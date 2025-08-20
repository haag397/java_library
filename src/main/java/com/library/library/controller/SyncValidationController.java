package com.library.library.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.library.dto.check_info.ValidateUserRequest;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SyncValidationController {

    private final ZeebeClient zeebe;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static class ValidateUserRequest {
        public String username;
        public String firstName;
        public String lastName;
        public Integer age;
        public String correlationKey; // optional
    }

    @PostMapping("/validate-sync")
    public ResponseEntity<Map<String, Object>> validateSync(@RequestBody ValidateUserRequest req) {
        String ck = (req.correlationKey == null || req.correlationKey.isBlank())
                ? UUID.randomUUID().toString() : req.correlationKey;

        Map<String, Object> vars = new HashMap<>();
        vars.put("username", req.username);
        vars.put("firstName", req.firstName);
        vars.put("lastName", req.lastName);
        vars.put("age", req.age);
        vars.put("correlationKey", ck);
        vars.put("responses", new HashMap<>()); // جمع‌کنندهٔ نتایج

        try {
            ProcessInstanceResult result = zeebe.newCreateInstanceCommand()
                    .bpmnProcessId("Process_0y1gajx")
                    .latestVersion()
                    .variables(vars)
                    .withResult()
                    .requestTimeout(Duration.ofSeconds(60))
                    .send()
                    .join();

            Map<String, Object> allVars = extractVarsPortable(result); // سازگار با نسخه‌های مختلف کلاینت

            Map<String, Object> body = Map.of(
                    "correlationKey", ck,
                    "responses", allVars.getOrDefault("responses", Map.of()),
                    "aggregated", allVars.getOrDefault("aggregated", Map.of())
            );
            return ResponseEntity.ok(body);

        } catch (Exception e) {
            return ResponseEntity.status(504).body(Map.of(
                    "error", "PROCESS_TIMEOUT_OR_ERROR",
                    "message", e.getMessage(),
                    "correlationKey", ck
            ));
        }
    }

    // سازگار با نسخه‌های مختلف Zeebe Client
    private static Map<String, Object> extractVarsPortable(ProcessInstanceResult r) throws Exception {
        try {
            return (Map<String, Object>) r.getClass().getMethod("getVariablesAsMap").invoke(r);
        } catch (NoSuchMethodException ignore) { /* fallthrough */ }

        try {
            String json = (String) r.getClass().getMethod("getVariables").invoke(r);
            return MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (NoSuchMethodException ignore) { /* fallthrough */ }

        Object doc = r.getClass().getMethod("getVariablesAsDocument").invoke(r);
        String json = (String) doc.getClass().getMethod("toJson").invoke(doc);
        return MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {});
    }
}