package com.library.library.camunda;

import com.library.library.command.RejectLoanCommand;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RejectLoanWorker {

    private final CommandGateway commandGateway;

    @JobWorker(type = "reject-loan", autoComplete = false)
    public void handle(JobClient client, ActivatedJob job) {
        Map<String, Object> vars = job.getVariablesAsMap();
        String loanId = (String) vars.get("loanId");

        System.out.println("[RejectLoan] loanId=" + loanId + " -> rejected");

        // send an Axon command (domain) to mark loan rejected
        commandGateway.send(new RejectLoanCommand(loanId, "Credit score too low"));

        client.newCompleteCommand(job.getKey()).send().join();

    }
}