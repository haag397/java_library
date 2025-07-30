package com.library.library.camunda;


import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AddWorker {

    @JobWorker(type = "add")
    public Map<String, Object> handle(ActivatedJob job) {
        return Map.of("result", "added");
    }
}