package com.library.library.camunda;

import com.library.library.service.CheckService;
import com.library.library.service.auth.AuthService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CheckValueWorker {
//    private final CheckService checkService;

    @JobWorker(type = "check value")
    public Map<String, Object> handleJob(ActivatedJob job) {
        String input = (String) job.getVariablesAsMap().get("valueToCheck");
        try {
            if ("slow".equalsIgnoreCase(input)) {
                Thread.sleep(11000); // 11 seconds → will trigger boundary timer
            } else {
                Thread.sleep(2000); // 2 seconds → completes before timer
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return Map.of("checkStatus", input);
    }
}