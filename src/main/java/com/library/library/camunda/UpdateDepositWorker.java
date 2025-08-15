//package com.library.library.camunda;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.library.library.command.accountmangement.UpdateAccountCommand;
//import com.library.library.dto.account_management_dto.UpdateDepositRequestDTO;
//import io.camunda.zeebe.client.api.response.ActivatedJob;
//import io.camunda.zeebe.client.api.worker.JobClient;
//import io.camunda.zeebe.spring.client.annotation.JobWorker;
//import lombok.RequiredArgsConstructor;
//import org.axonframework.commandhandling.gateway.CommandGateway;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.ExecutionException;
//
//@Component
//@RequiredArgsConstructor
//public class UpdateDepositWorker {
//
//    private final DepositUpdateService depositUpdateService;
//
//    @JobWorker(type = "account management")
//    public void handle(final JobClient client, final ActivatedJob job) {
//        Map<String, Object> vars = job.getVariablesAsMap();
//
//        UUID depositId = UUID.fromString((String) vars.get("depositId"));
//        boolean forceUpdate = Boolean.parseBoolean(String.valueOf(vars.getOrDefault("forceUpdate", "false")));
//
//        DepositResponseDTO result = depositUpdateService.updateDeposit(depositId, forceUpdate);
//
//        client.newCompleteCommand(job.getKey())
//                .variables(Map.of("updatedDeposit", result))
//                .send()
//                .join();
//    }
//    }
//}