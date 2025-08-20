package com.library.library.controller;

import io.camunda.zeebe.client.ZeebeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ResultController {

    private final ZeebeClient zeebe;

    @GetMapping("/result/{processInstanceKey}")
    public ResponseEntity<Map<String, Object>> getResult(@PathVariable long processInstanceKey) {
        // ⚠️ ZeebeClient does NOT let you query variables directly!
        // You need to read variables from either:
        //   1. Operate API (Camunda 8 SaaS/EE),
        //   2. Elasticsearch (self-managed),
        //   3. Or persist results in your own DB during workers.

        // Simplest demo (pretend we have them locally):
        throw new UnsupportedOperationException("Direct Zeebe variable fetch not supported. See note below.");
    }
}
