//package com.library.library.controller.worker_with_intermediate_catch;
//
//import com.library.library.command.intermediate_catch.ProvideUserData;
//import com.library.library.command.intermediate_catch.StartLoanApplication;
//import io.camunda.zeebe.client.ZeebeClient;
//import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
//import lombok.RequiredArgsConstructor;
//import org.axonframework.commandhandling.gateway.CommandGateway;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.Duration;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/flow/async")
//@RequiredArgsConstructor
//public class FlowAsyncController {
//
//    private final ZeebeClient zeebe;
//    private final CommandGateway commands;
//
//    @PostMapping("/start")
//    public ResponseEntity<?> start(@RequestBody StartRequest req) {
//        // 1) create aggregate
//        commands.sendAndWait(new StartLoanApplication(req.applicationId(), req.accountId()));
//
//        // 2) start process (sets correlationKey variable)
//        ProcessInstanceEvent evt = zeebe.newCreateInstanceCommand()
//                .bpmnProcessId("Process_0s48g9c")
//                .latestVersion()
//                .variables(Map.of(
//                        "applicationId", req.applicationId(),
//                        "accountId", req.accountId(),
//                        "correlationKey", req.applicationId()
//                ))
//                .send()
//                .join();
//
//        return ResponseEntity.ok(Map.of(
//                "processInstanceKey", evt.getProcessInstanceKey(),
//                "applicationId", req.applicationId()
//        ));
//    }
//
//    @PostMapping("/message")
//    public ResponseEntity<?> message(@RequestBody MessageRequest req) {
//        // publish Zeebe message to release the intermediate catch
//        zeebe.newPublishMessageCommand()
//                .messageName("user-data-received")
//                .correlationKey(req.applicationId())
//                .timeToLive(Duration.ofMinutes(10))
//                .variables(Map.of("userProvidedData", req.payload()))
//                .send().;
//
//        // update aggregate that user data exists (optional here or inside worker)
//        commands.sendAndWait(new ProvideUserData(req.applicationId(), req.payload()));
//
//        return ResponseEntity.accepted().body(Map.of("status", "published"));
//    }
//
//    public record StartRequest(String applicationId, String accountId) {}
//    public record MessageRequest(String applicationId, Object payload) {}
//}