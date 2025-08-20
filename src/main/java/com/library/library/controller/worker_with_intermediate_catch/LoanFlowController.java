package com.library.library.controller.worker_with_intermediate_catch;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/loan")
@RequiredArgsConstructor
public class LoanFlowController {
    private final LoanFlowService service;

    @PostMapping("/start")
    public ResponseEntity<Map<String,Object>> start(@RequestBody StartReq req){
        return ResponseEntity.ok(service.start(req.applicationId(), req.accountId()));
    }

    @PostMapping("/message")
    public ResponseEntity<?> message(@RequestBody MsgReq req){
        service.publishUserData(req.applicationId(), req.payload());
        return ResponseEntity.accepted().body(Map.of("status","published"));
    }

    public record StartReq(String applicationId, String accountId){}
    public record MsgReq(String applicationId, Object payload){}
}