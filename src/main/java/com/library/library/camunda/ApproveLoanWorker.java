package com.library.library.camunda;

import com.library.library.command.LoanApprovedCommand;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ApproveLoanWorker {

    private final CommandGateway commandGateway;

    public ApproveLoanWorker(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @JobWorker(type = "approve-loan", autoComplete = false)
    public void handle(JobClient client, ActivatedJob job) {
        Map<String, Object> vars = job.getVariablesAsMap();
        String loanId = (String) vars.get("loanId");

        System.out.println("[ApproveLoan] loanId=" + loanId + " -> approved");

        // optionally publish an Axon event/command for domain model
        commandGateway.send(new LoanApprovedCommand(loanId));

        client.newCompleteCommand(job.getKey()).send().join();
    }
}