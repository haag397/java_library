package com.library.library.camunda.check_info;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.library.dto.check_info.TaskResult;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AggregateWorker {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private TaskResult toTaskResult(Object o, TaskResult fallback) {
        if (o == null) return fallback;
        if (o instanceof TaskResult tr) return tr;
        // اغلب مقدار یک Map است که از JSON آمده؛ امن به TaskResult تبدیل می‌کنیم
        return MAPPER.convertValue(o, TaskResult.class);
    }

    @JobWorker(type = "Aggregate Responses", autoComplete = false)
    @SuppressWarnings("unchecked")
    public void handle(final JobClient client, final ActivatedJob job) {
        Map<String, Object> vars = job.getVariablesAsMap();
        Map<String, Object> responses =
                (Map<String, Object>) vars.getOrDefault("responses", Map.of());

        TaskResult t1 = toTaskResult(responses.get("task1"),
                new TaskResult(false, "MISSING", "no task1"));
        TaskResult t2 = toTaskResult(responses.get("task2"),
                new TaskResult(false, "MISSING", "no task2"));
        TaskResult t3 = toTaskResult(responses.get("task3"),
                new TaskResult(false, "MISSING", "no task3"));
        TaskResult t4 = toTaskResult(responses.get("task4"),
                new TaskResult(false, "MISSING", "no task4"));

        boolean allOk = t1.isOk() && t2.isOk() && t3.isOk() && t4.isOk();

        vars.put("aggregated", Map.of(
                "task1", t1,
                "task2", t2,
                "task3", t3,
                "task4", t4,
                "allOk", allOk
        ));

        client.newCompleteCommand(job.getKey())
                .variables(vars)
                .send()
                .join();
    }
}