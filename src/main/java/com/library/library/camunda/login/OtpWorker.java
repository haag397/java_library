package com.library.library.camunda.login;

import com.library.library.controller.login.OtpService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;

@Component
@Slf4j
@RequiredArgsConstructor
public class OtpWorker {
    private final OtpService otpService;

    @JobWorker(type = "send_otp")
    public void handleSendOtp(JobClient client, ActivatedJob job) {
        Map<String, Object> vars = job.getVariablesAsMap();
        String otp = String.format("%06d", new Random().nextInt(1000000));
        log.info("Generated OTP for process {}: {}, set sendOtpStatus=SUCCESS", job.getProcessInstanceKey(), otp);
        client.newCompleteCommand(job.getKey())
                .variables(Map.of(
                        "otp", otp,
                        "sendOtpStatus", "SUCCESS"
                ))
                .send()
                .join();
        otpService.updateTaskStatus(job.getProcessInstanceKey(), "sendOtpStatus", "SUCCESS");
    }
}