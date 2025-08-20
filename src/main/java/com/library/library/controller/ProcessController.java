//package com.library.library.controller;
//
//import com.library.library.dto.ProcessStartRequest;
//import io.camunda.zeebe.client.ZeebeClient;
//import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
//import io.camunda.zeebe.client.api.response.ProcessInstanceResult;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.Duration;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/process")
//@RequiredArgsConstructor
//public class ProcessController {
//
//    private final ZeebeClient zeebeClient;
//
//    @PostMapping("/start")
////    public ResponseEntity<Map<String, Object>> startProcess(@RequestBody ProcessStartRequest request) {
//        public ResponseEntity<String> startProcess(@RequestBody ProcessStartRequest request) {
//        Duration timeout = Duration.ofSeconds(20);
//        Map<String, Object> variables = Map.of("valueToCheck", request.getValueToCheck());
////        ProcessInstanceEvent result = zeebeClient.newCreateInstanceCommand()
//        ProcessInstanceEvent instance = zeebeClient.newCreateInstanceCommand()
//
////        ProcessInstanceResult result = zeebeClient.newCreateInstanceCommand()
//                .bpmnProcessId("timeOut")
//                .latestVersion()
//                .variables(variables)
////                .withResult()
////                .requestTimeout(timeout)  // Set timeout explicitly
//                .send()
//                .join();
//
////        return ResponseEntity.ok(result.getVariablesAsMap());
//        return ResponseEntity.ok("Started process with instance key: " + instance.getProcessInstanceKey());
//    }
//}
