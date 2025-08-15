package com.library.library.repository;

import com.library.library.dto.account_management_dto.DepositResponseDTO;
import com.library.library.dto.account_management_dto.DepositSaveRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "core-deposit", url = "${core.deposit.url}")
public interface CoreDepositClient {
    @GetMapping("/deposits/{userId}/{depositNumber}")
    DepositSaveRequestDTO fetchDeposit(
            @PathVariable String userId,
            @PathVariable String depositNumber
    );
}