package com.library.library.controller.worker_with_intermediate_catch;

import com.library.library.command.intermediate_catch.ProvideUserData;
import com.library.library.command.intermediate_catch.StartLoanApplication;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoanFlowService {
    private final ZeebeClient zeebe;
    private final CommandGateway commands;

    public Map<String,Object> start(String applicationId, String accountId) {
        commands.sendAndWait(new StartLoanApplication(applicationId, accountId));
        ProcessInstanceEvent evt = zeebe.newCreateInstanceCommand()
                .bpmnProcessId("Process_0s48g9c")
                .latestVersion()
                .variables(Map.of(
                        "applicationId", applicationId,
                        "accountId", accountId,
                        "correlationKey", applicationId  // used by message catch
                ))
                .send().join();
        return Map.of("processInstanceKey", evt.getProcessInstanceKey(), "applicationId", applicationId);
    }

    public void publishUserData(String applicationId, Object payload) {
        zeebe.newPublishMessageCommand()
                .messageName("user-data-received")
                .correlationKey(applicationId)         // must match zeebe:subscription correlationKey
                .timeToLive(Duration.ofMinutes(10))
                .variables(Map.of("userProvidedData", payload)) // becomes available to next task
                .send().join();
    }
}