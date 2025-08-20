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
public class Task3Worker {

    private final ZeebeClient zeebe;

    @JobWorker(type = "Service Task 3", autoComplete = false)
    @SuppressWarnings("unchecked")
    public void handle(final JobClient client, final ActivatedJob job) {
        Map<String, Object> vars = job.getVariablesAsMap();
        Map<String, Object> responses =
                (Map<String, Object>) vars.getOrDefault("responses", new HashMap<>());

        Integer age = (Integer) vars.get("age");

        if (age == null || age < 18 || age > 70) {
            responses.put("task3", new TaskResult(false, "AGE_RANGE",
                    "age must be between 18 and 70"));

            zeebe.newSetVariablesCommand(job.getProcessInstanceKey())
                    .variables(Map.of("responses", responses))
                    .send().join();

            client.newThrowErrorCommand(job)
                    .errorCode("E_TASK3")
                    .errorMessage("invalid age")
                    .send().join();
            return;
        }

        responses.put("task3", new TaskResult(true, null, "age ok"));
        vars.put("responses", responses);

        client.newCompleteCommand(job.getKey())
                .variables(vars)
                .send().join();
    }
}
