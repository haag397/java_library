package com.library.library.camunda;

import com.library.library.dto.BpmnErrorDto;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CheckBalanceWorker {

    @JobWorker(type = "check-balance")
    public void checkBalance(ActivatedJob job, JobClient client) {
        Map<String, Object> variables = job.getVariablesAsMap();
        String userId = (String) variables.get("userId");

        // Simulate a check
        boolean balanceIsLow = true; // Replace with real business logic

        if (balanceIsLow) {
            BpmnErrorDto error = new BpmnErrorDto();
            client.newThrowErrorCommand(job.getKey())
                    .errorCode(error.getErrorCode())
                    .errorMessage(error.getErrorMessage())
                    .send()
                    .join();
            return;
        }

        // If balance is OK
        client.newCompleteCommand(job.getKey())
                .variables(Map.of("balanceStatus", "OK"))
                .send()
                .join();
    }
}
