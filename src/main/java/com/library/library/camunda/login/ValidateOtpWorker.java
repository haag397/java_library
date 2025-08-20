package com.library.library.camunda.login;

import com.library.library.controller.login.OtpService;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class ValidateOtpWorker {
    private final OtpService otpService;

    @JobWorker(type = "validate_otp", autoComplete = false, timeout = 180000) // 3 minutes TTL
    public void handleValidateOtp(JobClient client, ActivatedJob job) {
        otpService.updateTaskStatus(job.getProcessInstanceKey(), "validateOtpStatus", "PENDING");
        otpService.addPendingJob(job.getProcessInstanceKey(), job);
        log.info("Stored job for process {}, set validateOtpStatus=PENDING", job.getProcessInstanceKey());
    }
}