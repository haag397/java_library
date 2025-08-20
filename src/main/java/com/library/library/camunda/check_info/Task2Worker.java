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
public class Task2Worker {

    private final ZeebeClient zeebe;

    private static boolean hasForbidden(String s) {
        return s != null && s.matches(".*[\\*&%#@+].*");
    }

    @JobWorker(type = "Service Task 2", autoComplete = false)
    @SuppressWarnings("unchecked")
    public void handle(final JobClient client, final ActivatedJob job) {
        Map<String, Object> vars = job.getVariablesAsMap();
        Map<String, Object> responses =
                (Map<String, Object>) vars.getOrDefault("responses", new HashMap<>());

        String fn = (String) vars.get("firstName");
        String ln = (String) vars.get("lastName");

        if (hasForbidden(fn) || hasForbidden(ln)) {
            responses.put("task2", new TaskResult(false, "NAME_SPECIAL_CHARS",
                    "firstName/lastName must not contain *&%#@+"));

            zeebe.newSetVariablesCommand(job.getProcessInstanceKey())
                    .variables(Map.of("responses", responses))
                    .send().join();

            client.newThrowErrorCommand(job)
                    .errorCode("E_TASK2")
                    .errorMessage("invalid characters in name")
                    .send().join();
            return;
        }

        responses.put("task2", new TaskResult(true, null, "names ok"));
        vars.put("responses", responses);

        client.newCompleteCommand(job.getKey())
                .variables(vars)
                .send().join();
    }
}


