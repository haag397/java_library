package com.library.library.controller.login;

import com.library.library.dto.auth.AuthenticationRequestDTO;
import com.library.library.exception.UserNotFoundException;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {
    private final ZeebeClient zeebeClient;
    private final OtpService otpService;

    @PostMapping("/start-authentication")
    public ResponseEntity<Map<String, Object>> startAuthentication(@RequestBody AuthenticationRequestDTO dto) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("email", dto.getEmail());
        variables.put("password", dto.getPassword());
        variables.put("retryCount", 0);

        ProcessInstanceEvent pi = zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId("login")
                .latestVersion()
                .variables(variables)
                .send()
                .join();

        Long piKey = pi.getProcessInstanceKey();

        long startTime = System.currentTimeMillis();
        Map<String, Object> response = new HashMap<>();
        response.put("processInstanceKey", piKey);

        // Poll for task statuses (increased timeout to 10s)
        while (System.currentTimeMillis() - startTime < 10000) {
            ActivatedJob job = otpService.getPendingValidationJobs().get(piKey);
            if (job != null) {
                Map<String, Object> vars = job.getVariablesAsMap();
                String otp = (String) vars.get("otp");
                response.put("otp", otp);
                Map<String, String> statuses = otpService.getTaskStatuses(piKey);
                response.put("authenticationStatus", statuses.getOrDefault("authenticationStatus", "UNKNOWN"));
                response.put("sendOtpStatus", statuses.getOrDefault("sendOtpStatus", "UNKNOWN"));
                response.put("validateOtpStatus", statuses.getOrDefault("validateOtpStatus", "PENDING"));
                if ("FAILED".equals(statuses.get("authenticationStatus"))) {
                    response.put("errorMessage", vars.getOrDefault("errorMessage", "Authentication failed"));
                    return ResponseEntity.badRequest().body(response);
                }
                return ResponseEntity.ok(response);
            }

            // Check task statuses from OtpService
            Map<String, String> statuses = otpService.getTaskStatuses(piKey);
            if (statuses.containsKey("authenticationStatus")) {
                response.put("authenticationStatus", statuses.get("authenticationStatus"));
                if ("FAILED".equals(statuses.get("authenticationStatus"))) {
                    response.put("errorMessage", "Authentication failed");
                    return ResponseEntity.badRequest().body(response);
                }
            }
            if (statuses.containsKey("sendOtpStatus")) {
                response.put("sendOtpStatus", statuses.get("sendOtpStatus"));
            }
            if (statuses.containsKey("validateOtpStatus")) {
                response.put("validateOtpStatus", statuses.get("validateOtpStatus"));
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted");
            }
        }

        response.put("error", "Process did not reach OTP generation");
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping(value = "/verify-otp", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> verifyOtp(
            @RequestParam Long processInstanceKey,
            @RequestBody Map<String, String> requestBody) {
        String otp = requestBody.get("otp");
        if (otp == null || otp.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "OTP is required"));
        }

        ActivatedJob job = otpService.getPendingValidationJobs().remove(processInstanceKey);
        Map<String, String> statuses = otpService.getTaskStatuses(processInstanceKey);
        if (job == null) {
            if (statuses.containsKey("validateOtpStatus") && "PENDING".equals(statuses.get("validateOtpStatus"))) {
                otpService.updateTaskStatus(processInstanceKey, "validateOtpStatus", "TIMEOUT");
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "OTP validation timed out",
                        "authenticationStatus", statuses.getOrDefault("authenticationStatus", "UNKNOWN"),
                        "sendOtpStatus", statuses.getOrDefault("sendOtpStatus", "UNKNOWN"),
                        "validateOtpStatus", "TIMEOUT"
                ));
            }
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "No pending OTP validation",
                    "authenticationStatus", statuses.getOrDefault("authenticationStatus", "UNKNOWN"),
                    "sendOtpStatus", statuses.getOrDefault("sendOtpStatus", "UNKNOWN"),
                    "validateOtpStatus", "UNKNOWN"
            ));
        }

        String expectedOtp = (String) job.getVariablesAsMap().get("otp");
        boolean valid = expectedOtp.equals(otp.trim());
        otpService.updateTaskStatus(processInstanceKey, "validateOtpStatus", valid ? "SUCCESS" : "FAILED");

        Map<String, Object> vars = new HashMap<>();
        vars.put("otpValid", valid);
        vars.put("validateOtpStatus", valid ? "SUCCESS" : "FAILED");

        zeebeClient.newCompleteCommand(job.getKey())
                .variables(vars)
                .send()
                .join();

        if (valid) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "logined");
            response.put("accessToken", job.getVariablesAsMap().get("accessToken"));
            response.put("refreshToken", job.getVariablesAsMap().get("refreshToken"));
            response.put("authenticationStatus", statuses.getOrDefault("authenticationStatus", "UNKNOWN"));
            response.put("sendOtpStatus", statuses.getOrDefault("sendOtpStatus", "UNKNOWN"));
            response.put("validateOtpStatus", "SUCCESS");
            return ResponseEntity.ok(response);
        } else {
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < 2000) {
                ActivatedJob newJob = otpService.getPendingValidationJobs().get(processInstanceKey);
                if (newJob != null) {
                    String newOtp = (String) newJob.getVariablesAsMap().get("otp");
                    return ResponseEntity.ok(Map.of(
                            "valid", false,
                            "message", "Invalid OTP, new OTP generated",
                            "otp", newOtp,
                            "processInstanceKey", processInstanceKey,
                            "authenticationStatus", statuses.getOrDefault("authenticationStatus", "UNKNOWN"),
                            "sendOtpStatus", statuses.getOrDefault("sendOtpStatus", "UNKNOWN"),
                            "validateOtpStatus", "FAILED"
                    ));
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted");
                }
            }
            otpService.updateTaskStatus(processInstanceKey, "validateOtpStatus", "FAILED");
            return ResponseEntity.badRequest().body(Map.of(
                    "valid", false,
                    "message", "Max retries exceeded",
                    "authenticationStatus", statuses.getOrDefault("authenticationStatus", "UNKNOWN"),
                    "sendOtpStatus", statuses.getOrDefault("sendOtpStatus", "UNKNOWN"),
                    "validateOtpStatus", "FAILED"
            ));
        }
    }
}