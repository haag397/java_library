package com.library.library.camunda.check_info;

import com.library.library.dto.check_info.TaskResult;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Task1Worker {

    private final ZeebeClient zeebe;

    @JobWorker(type = "Service Task 1", autoComplete = false)
    @SuppressWarnings("unchecked")
    public void handle(final JobClient client, final ActivatedJob job) {
        Map<String, Object> vars = job.getVariablesAsMap();
        Map<String, Object> responses =
                (Map<String, Object>) vars.getOrDefault("responses", new HashMap<>());

        String username = (String) vars.get("username");

        if (username != null && username.length() > 8) {
            responses.put("task1", new TaskResult(false, "USERNAME_LENGTH", "username must be ≤ 8 chars"));

            zeebe.newSetVariablesCommand(job.getProcessInstanceKey())
                    .variables(Map.of("responses", responses))
                    .send().join();

            client.newThrowErrorCommand(job)
                    .errorCode("E_TASK1")
                    .errorMessage("username too long")
                    .send().join();
            return;
        }

        responses.put("task1", new TaskResult(true, null, "username ok"));
        vars.put("responses", responses);

        client.newCompleteCommand(job.getKey())
                .variables(vars)
                .send().join();
    }
}

