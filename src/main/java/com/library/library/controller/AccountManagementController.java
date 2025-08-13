package com.library.library.controller;

import com.library.library.command.UpdateAccountCommand;
import com.library.library.dto.user.UpdateAccountRequestDTO;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/deposits")
@RequiredArgsConstructor
public class AccountManagementController {

    private final CommandGateway commandGateway;

    // REST endpoint to manually trigger an update
    @PostMapping("/{depositId}/update")
    public CompletableFuture<String> updateDeposit(@PathVariable UUID depositId, @RequestBody UpdateAccountRequestDTO request) {
        UpdateAccountCommand command = UpdateAccountCommand.builder()
                .depositId(depositId)
                .updateAccountRequest(request)
                .build();
        return commandGateway.send(command);
    }
}