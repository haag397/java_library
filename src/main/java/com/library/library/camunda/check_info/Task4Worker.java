package com.library.library.camunda.check_info;

import com.library.library.dto.check_info.TaskResult;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Task4Worker {

    private final ZeebeClient zeebe;

    @JobWorker(type = "Service Task 4", autoComplete = false)
    @SuppressWarnings("unchecked")
    public void handle(final JobClient client, final ActivatedJob job) {
        Map<String, Object> vars = job.getVariablesAsMap();
        Map<String, Object> responses =
                (Map<String, Object>) vars.getOrDefault("responses", new HashMap<>());

        String username = (String) vars.get("username");
        String fn = (String) vars.get("firstName");
        String ln = (String) vars.get("lastName");

        if (username != null &&
                (username.equalsIgnoreCase(fn) || username.equalsIgnoreCase(ln))) {
            responses.put("task4", new TaskResult(false, "USERNAME_EQUALS_NAME",
                    "username must not equal firstName or lastName"));

            zeebe.newSetVariablesCommand(job.getProcessInstanceKey())
                    .variables(Map.of("responses", responses))
                    .send().join();

            client.newThrowErrorCommand(job)
                    .errorCode("E_TASK4")
                    .errorMessage("username equals firstname/lastname")
                    .send().join();
            return;
        }

        responses.put("task4", new TaskResult(true, null, "username different from names"));
        vars.put("responses", responses);

        client.newCompleteCommand(job.getKey())
                .variables(vars)
                .send().join();
    }
}

