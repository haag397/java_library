package com.library.library.camunda;

import com.library.library.dto.account_management_dto.DepositResponseDTO;
import com.library.library.service.account_management_service.DepositQueryService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DepositUpdateWorker {

    private final DepositQueryService service;

    @JobWorker(type = "update-deposit", autoComplete = true)
    public DepositResponseDTO handle(final ActivatedJob job) {
        Map<String, Object> vars = job.getVariablesAsMap();
        UUID depositId = UUID.fromString((String) vars.get("depositId"));
        String userId = (String) vars.get("userId");
        String depositNumber = (String) vars.get("depositNumber");
        boolean forceUpdate = Boolean.TRUE.equals(vars.get("forceUpdate")) ||
                "true".equalsIgnoreCase(String.valueOf(vars.get("forceUpdate")));

        // Reuse exactly the same logic as the controller path:
        return service.getDeposit(depositId, userId, depositNumber, forceUpdate);
    }
}
