package com.library.library.camunda;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.library.command.UpdateAccountCommand;
import com.library.library.dto.user.UpdateAccountRequestDTO;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class AccountManagementWorker {

    private final CommandGateway commandGateway;
    private final ObjectMapper objectMapper; // Spring Boot autoconfigures this

    @JobWorker(type = "account management", autoComplete = false)
    public void handleJob(final JobClient client, final ActivatedJob job) throws Exception {
        System.out.println("Processing Zeebe job with type: " + job.getType());

        try {
            // Extract variables from the Zeebe job.
            // We assume the DTO is passed as a single JSON variable named 'updateData'
            String updateDataJson = job.getVariables();
            UpdateAccountRequestDTO updateRequest = objectMapper.readValue(updateDataJson, UpdateAccountRequestDTO.class);

            // You must get the depositId from the job variables
            UUID depositId = UUID.fromString(job.getCustomHeaders().get("depositId"));

            // Create and send the command to the Axon CommandGateway
            UpdateAccountCommand command = UpdateAccountCommand.builder()
                    .depositId(depositId)
                    .updateAccountRequest(updateRequest)
                    .build();

            // The send method returns a CompletableFuture, which we can wait for
            commandGateway.send(command).get();

            // IMPORTANT: Since autoComplete = false, we MUST manually complete the job
            client.newCompleteCommand(job.getKey()).send().join();
            System.out.println("Zeebe job completed successfully for depositId: " + depositId);

        } catch (InterruptedException | ExecutionException e) {
            // If the command processing fails, throw a custom exception
            // to signal to Zeebe that the job should be retried.
            System.err.println("Failed to process command for Zeebe job: " + e.getMessage());
            throw new RuntimeException("Failed to process account update command", e);
        }
    }
}