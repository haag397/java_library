package com.library.library.aggregate.user_aggregate;

import com.library.library.command.check_info.PublishMessageCommand;
import com.library.library.command.check_info.StartValidationCommand;
import io.camunda.zeebe.client.ZeebeClient;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProcessCommandHandler {

    private final ZeebeClient zeebe; // injected by spring-zeebe-starter

    @CommandHandler
    public long handle(StartValidationCommand cmd) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("username", cmd.username());
        vars.put("firstName", cmd.firstName());
        vars.put("lastName", cmd.lastName());
        vars.put("age", cmd.age());
        vars.put("correlationKey", cmd.correlationKey());
        vars.put("responses", new HashMap<>()); // shared collector

        var resp = zeebe.newCreateInstanceCommand()
                .bpmnProcessId("Process_0y1gajx")
                .latestVersion()
                .variables(vars)
                .send()
                .join();

        return resp.getProcessInstanceKey();
    }

    @CommandHandler
    public void handle(PublishMessageCommand cmd) {
        zeebe.newPublishMessageCommand()
                .messageName("Message_21b64fk")
                .correlationKey(cmd.correlationKey())
                .timeToLive(Duration.ofMinutes(10))
                .variables(cmd.payload())
                .send()
                .join();
    }
}
