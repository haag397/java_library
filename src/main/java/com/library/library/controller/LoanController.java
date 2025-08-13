package com.library.library.controller;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loans")
public class LoanController {

    private final ZeebeClient zeebeClient;

    @PostMapping("/apply")
    public ResponseEntity<String> applyLoan(@RequestParam int creditScore) {
        String loanId = UUID.randomUUID().toString();

        Map<String, Object> variables = Map.of(
                "loanId", loanId,
                "creditScore", creditScore
        );

        ProcessInstanceEvent created = zeebeClient
                .newCreateInstanceCommand()
                .bpmnProcessId("loanApprovalProcess")  // must match your bpmn process id
                .latestVersion()
                .variables(variables)
                .send()
                .join();

        return ResponseEntity.ok(
                String.format("Started process. processInstanceKey=%d loanId=%s", created.getProcessInstanceKey(), loanId)
        );
    }
}
