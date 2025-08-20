package com.library.library.camunda.grok;

import com.library.library.command.grok.CheckingCommand;
import com.library.library.command.grok.ValidationCommand;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ServiceTaskWorkers {

    private final CommandGateway commandGateway;

    @JobWorker(type = "validation")
    public void handleValidation(JobClient client, ActivatedJob job) {
        String processId = String.valueOf(job.getProcessInstanceKey());
        Map<String, Object> variables = job.getVariablesAsMap();
        String data = (String) variables.getOrDefault("data", "");

        try {
            commandGateway.sendAndWait(new ValidationCommand(processId, data));
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Validation completed successfully");
            response.put("data", data + "_validated");

            client.newCompleteCommand(job.getKey())
                    .variables(Map.of("validationResponse", response))
                    .send()
                    .join();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "failure");
            response.put("message", e.getMessage());

            client.newThrowErrorCommand(job.getKey())
                    .errorCode("VALIDATION_ERROR")
                    .errorMessage(e.getMessage())
                    .variables(Map.of("validationResponse", response))
                    .send()
                    .join();
        }
    }

    @JobWorker(type = "checking")
    public void handleChecking(JobClient client, ActivatedJob job) {
        String processId = String.valueOf(job.getProcessInstanceKey());
        Map<String, Object> variables = job.getVariablesAsMap();
        String data = (String) variables.getOrDefault("userData", "");

        try {
            commandGateway.sendAndWait(new CheckingCommand(processId, data));
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Checking completed successfully");
            response.put("data", data + "_checked");

            client.newCompleteCommand(job.getKey())
                    .variables(Map.of("checkingResponse", response))
                    .send()
                    .join();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "failure");
            response.put("message", e.getMessage());

            client.newThrowErrorCommand(job.getKey())
                    .errorCode("CHECKING_ERROR")
                    .errorMessage(e.getMessage())
                    .variables(Map.of("checkingResponse", response))
                    .send()
                    .join();
        }
    }
}