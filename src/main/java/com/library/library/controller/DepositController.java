package com.library.library.controller;

import com.library.library.dto.account_management_dto.UpdateDepositRequestDTO;
import com.library.library.dto.account_management_dto.UpdateDepositResponseDTO;
import com.library.library.service.account_management_service.DepositService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/deposits")
@AllArgsConstructor
public class DepositController {

    private final DepositService depositService;

    @PostMapping("/update")
    public CompletableFuture<ResponseEntity<UpdateDepositResponseDTO>> updateDeposit(
            @RequestBody UpdateDepositRequestDTO request) {

        // Get authenticated user ID if needed
        String userId = AuthenticationUtil.getAuthenticatedUser();

        return depositService
                .startUpdateFlowAndWaitForResult(request)
                .thenApply(ResponseEntity::ok);
    }


}