package com.library.library.controller.grok;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.DeploymentEvent;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.client.api.response.ProcessInstanceResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProcessController {

    private final ZeebeClient zeebeClient;

//    @PostMapping("/start-process")
//    public ResponseEntity<Map<String, Object>> startProcess(@RequestBody Map<String, Object> variables) {
//        try {
//            // شروع فرآیند با withResult
//            ProcessInstanceResult result = zeebeClient.newCreateInstanceCommand()
//                    .bpmnProcessId("Process_1")
//                    .latestVersion()
//                    .variables(variables)
//                    .withResult()
//                    // انتظار برای تکمیل فرآیند و دریافت متغیرها
////                    .requestTimeout(java.time.Duration.ofMinutes(6))
//                    .send()
//                    .join();
//
//            // دریافت متغیرهای خروجی
//            Map<String, Object> outputVariables = result.getVariablesAsMap();
//            outputVariables.put("instanceKey", result.getProcessInstanceKey());
//            outputVariables.put("note", "Process completed successfully");
//
//            return ResponseEntity.ok(outputVariables);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(Map.of("error", "Failed to start process: " + e.getMessage()));

    @PostMapping("/start-process")
    public ResponseEntity<Map<String, Object>>startProcess(@RequestBody Map<String, Object> variables) {
        try {
            // شروع ناهمزمان فرآیند
            ProcessInstanceEvent event = zeebeClient.newCreateInstanceCommand()
                    .bpmnProcessId("Process_1")
                    .latestVersion()
                    .variables(variables)
                    .send()
                    .join();

            return ResponseEntity.ok(Map.of(
                    "instanceKey", event.getProcessInstanceKey(),
                    "note", "Process started; complete User Task to proceed"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Failed to start process: " + e.getMessage()
            ));
        }
    }
}