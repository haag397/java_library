package com.library.library.controller;

import com.library.library.dto.account_management_dto.DepositResponseDTO;
import com.library.library.dto.account_management_dto.UpdateDepositRequestDTO;
import com.library.library.dto.account_management_dto.UpdateDepositResponseDTO;
import com.library.library.service.account_management_service.DepositQueryService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/deposits")
@RequiredArgsConstructor
public class DepositController {

    private final DepositQueryService service;

    @GetMapping("/{depositId}")
    public ResponseEntity<DepositResponseDTO> getDeposit(
            @PathVariable UUID depositId,
            @RequestParam String userId,
            @RequestParam String depositNumber,
            @RequestParam(defaultValue = "false") boolean forceUpdate) {

        DepositResponseDTO dto = service.getDeposit(depositId, userId, depositNumber, forceUpdate);
        return ResponseEntity.ok(dto);
    }
}
