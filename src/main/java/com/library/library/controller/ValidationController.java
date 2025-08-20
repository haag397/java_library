package com.library.library.controller;

import com.library.library.command.check_info.PublishMessageCommand;
import com.library.library.command.check_info.StartValidationCommand;
import com.library.library.dto.check_info.StartResponse;
import com.library.library.dto.check_info.ValidateUserRequest;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ValidationController {

    private final CommandGateway commandGateway;

    @PostMapping("/validate")
    public ResponseEntity<StartResponse> start(@RequestBody ValidateUserRequest req) {
        var ck = (req.correlationKey() != null && !req.correlationKey().isBlank())
                ? req.correlationKey()
                : UUID.randomUUID().toString();

        var cmd = new StartValidationCommand(
                req.username(), req.firstName(), req.lastName(), req.age(), ck);

        // fire-and-return the process instance key
        long piKey = commandGateway.sendAndWait(cmd);
        return ResponseEntity.accepted().body(new StartResponse(piKey, ck));
    }

    @PostMapping("/signal")
    public ResponseEntity<Void> signal(@RequestParam String correlationKey,
                                       @RequestBody(required=false) Map<String,Object> body) {
        commandGateway.sendAndWait(new PublishMessageCommand(correlationKey, body == null ? Map.of() : body));
        return ResponseEntity.accepted().build();
    }
}
