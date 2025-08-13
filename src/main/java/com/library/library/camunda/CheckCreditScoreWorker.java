package com.library.library.camunda;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CheckCreditScoreWorker {

    @JobWorker(type = "check-credit-score", autoComplete = false)
    public void handle(JobClient client, ActivatedJob job) {
        Map<String, Object> vars = job.getVariablesAsMap();
        Integer creditScore = vars.get("creditScore") instanceof Number ? ((Number) vars.get("creditScore")).intValue() : null;
        String loanId = (String) vars.get("loanId");

        System.out.println("[CheckCreditScore] loanId=" + loanId + " creditScore=" + creditScore);

        if (creditScore != null && creditScore < 600) {
            // Throw BPMN error so boundary event catches it.
            client.newThrowErrorCommand(job.getKey())
                    .errorCode("LOW_CREDIT_SCORE")
                    .errorMessage("Credit score too low")
                    .send()
                    .join();
            // job ended via error, no complete command.
            return;
        }

        // else complete the job and optionally set additional variables
        client.newCompleteCommand(job.getKey())
                .variables(Map.of("creditOk", true))
                .send()
                .join();
    }
}