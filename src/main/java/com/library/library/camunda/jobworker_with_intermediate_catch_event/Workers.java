package com.library.library.camunda.jobworker_with_intermediate_catch_event;

import com.library.library.aggregate.intermediate_catch.LoanApplication;
import com.library.library.command.intermediate_catch.GrantLoan;
import com.library.library.command.intermediate_catch.MarkValidationRequested;
import com.library.library.command.intermediate_catch.ProvideUserData;
import com.library.library.command.intermediate_catch.ValidateAccount;
import com.library.library.controller.worker_with_intermediate_catch.ProgressStreamService;
import com.library.library.dto.worker_with_intermediate_catch_dto.ProgressUpdate;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.common.exception.ZeebeBpmnError;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class Workers {

    private final CommandGateway commands;
    private final ProgressStreamService progress;

    private void tick(String appId, String taskType, String phase, String msg, Map<String,Object> data) {
        progress.emit(new ProgressUpdate(appId, taskType, phase, msg, data, Instant.now()));
    }

    /** <zeebe:taskDefinition type="account validation" /> */
    @JobWorker(type = "account validation", autoComplete = true)
    public Map<String,Object> accountValidation(final ActivatedJob job){
        var vars = job.getVariablesAsMap();
        String appId = (String) vars.get("applicationId");
        String accountId = (String) vars.get("accountId");

        tick(appId, "account validation", "STARTED", null, Map.of("accountId", accountId));
        try {
            commands.sendAndWait(new ValidateAccount(appId, accountId));
            tick(appId, "account validation", "COMPLETED", "Account valid", Map.of("accountValid", true));
            return Map.of("accountValid", true);

        } catch (LoanApplication.BusinessRuleViolation brv) {
            tick(appId, "account validation", "FAILED", brv.getMessage(), Map.of());
            throw new ZeebeBpmnError(brv.getMessage(), "Account failed rule", Map.of()); // e.g. ACCOUNT_INVALID
        }
    }

    /** <zeebe:taskDefinition type="request validation" /> */
    @JobWorker(type = "request validation", autoComplete = true)
    public Map<String,Object> requestValidation(final ActivatedJob job){
        var vars = job.getVariablesAsMap();
        String appId = (String) vars.get("applicationId");

        tick(appId, "request validation", "STARTED", null, Map.of());
        try {
            commands.sendAndWait(new MarkValidationRequested(appId));
            // send notification/email/SMS to user outside (omitted)
            tick(appId, "request validation", "COMPLETED", "User notified", Map.of("requestDispatched", true));
            return Map.of("requestDispatched", true);

        } catch (LoanApplication.BusinessRuleViolation brv) {
            tick(appId, "request validation", "FAILED", brv.getMessage(), Map.of());
            throw new ZeebeBpmnError(brv.getMessage(), "Request dispatch failed", Map.of()); // e.g. REQUEST_FAILED
        }
    }

    /** <zeebe:taskDefinition type="grant loan" />  (runs AFTER the message catch) */
    @JobWorker(type = "grant loan", autoComplete = false)
    public void grantLoan(final JobClient client, final ActivatedJob job){
        var vars = job.getVariablesAsMap();
        String appId  = (String) vars.get("applicationId");
        Object payload = vars.get("userProvidedData"); // <-- set by the intermediate message catch

        tick(appId, "grant loan", "STARTED", null, Map.of("hasPayload", payload != null));

        try {
            // Update domain with the now-available user data
            if (payload != null) {
                commands.sendAndWait(new ProvideUserData(appId, payload));
            }
            // Enforce invariants and finalize
            commands.sendAndWait(new GrantLoan(appId));

            client.newCompleteCommand(job.getKey())
                    .variables(Map.of("userValidated", true, "validationResult", "OK"))
                    .send().join();

            tick(appId, "grant loan", "COMPLETED", "User validated & loan granted",
                    Map.of("userValidated", true, "validationResult", "OK"));

            // Optional: close the SSE stream when process ends
            progress.complete(appId);

        } catch (LoanApplication.BusinessRuleViolation brv) {
            client.newThrowErrorCommand(job.getKey())
                    .errorCode(brv.getMessage())                // e.g. VALIDATION_FAILED
                    .errorMessage("Business rule violated")
                    .send().join();

            tick(appId, "grant loan", "FAILED", brv.getMessage(), Map.of());

        } catch (Exception e) {
            client.newFailCommand(job.getKey())
                    .retries(Math.max(0, job.getRetries() - 1))
                    .errorMessage(e.getMessage())
                    .send().join();

            tick(appId, "grant loan", "FAILED", e.getMessage(), Map.of("technical", true));
        }
    }
}