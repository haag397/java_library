package com.library.library.controller;

import com.library.library.dto.auth.*;
import com.library.library.exception.UserNotFoundException;
import com.library.library.repository.UsersRepository;
import com.library.library.service.auth.AuthService;
import io.camunda.zeebe.client.api.command.ClientException;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.client.api.response.ProcessInstanceResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.camunda.zeebe.client.ZeebeClient;

@RestController
@RequestMapping("/api/event/")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UsersRepository usersRepository;
    private final AuthService authService;

    private final ZeebeClient zeebeClient;
    private static final Duration TIMEOUT = Duration.ofSeconds(30);

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
        return ResponseEntity.ok(authService.registers(registerRequestDTO));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticates(@RequestBody AuthenticationRequestDTO dto) {
        if (!usersRepository.existsByEmail(dto.getEmail())) {
            throw new UserNotFoundException();
        }
        return ResponseEntity.ok(authService.auth(dto));
    }

    @PostMapping("/start-registration")
    public ResponseEntity<Void> startRegistration(@RequestBody RegisterRequestDTO dto) {
        Map<String, Object> variables = Map.of(
                "firstName", dto.getFirstName(),
                "lastName", dto.getLastName(),
                "email", dto.getEmail(),
                "mobile", dto.getMobile(),
                "password", dto.getPassword(),
                "role", dto.getRole().name()
        );
        zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId("user-register")
                .latestVersion()
                .variables(variables)
                .send()
                .join();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/start-authentication")
    public ResponseEntity<AuthResponse> authenticate(
            @Valid @RequestBody AuthenticationRequestDTO request) {

        try {
            log.info("Authentication request received for email: {}", request.getEmail());
            ProcessInstanceEvent processInstance = zeebeClient
                    .newCreateInstanceCommand()
                    .bpmnProcessId("authentication-process")
                    .latestVersion()
                    .variables(Map.of(
                            "email", request.getEmail(),
                            "password", request.getPassword()
                    ))
                    .send()
                    .join();

            log.info("Started authentication process with instance ID: {}",
                    processInstance.getProcessInstanceKey());
            CompletableFuture<Map<String, Object>> resultFuture = waitForProcessCompletion(
                    processInstance.getProcessInstanceKey());

            Map<String, Object> result = resultFuture.get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

            String status = (String) result.get("status");

            if ("SUCCESS".equals(status)) {
                AuthResponse response = AuthResponse.builder()
                        .accessToken((String) result.get("accessToken"))
                        .refreshToken((String) result.get("refreshToken"))
                        .userId((UUID) result.get("userId"))
                        .email((String) result.get("email"))
                        .build();

                log.info("Authentication successful for user: {}", request.getEmail());
                return ResponseEntity.ok(response);

            } else {
                String errorCode = (String) result.get("errorCode");
                String errorMessage = (String) result.get("errorMessage");

                log.error("Authentication failed for user: {} - {}", request.getEmail(), errorMessage);

                return handleAuthenticationError(errorCode, errorMessage);
            }

        } catch (UserNotFoundException e) {
            log.error("User not found: {}", e.getMessage());
            throw new UserNotFoundException();

        } catch (Exception e) {
            log.error("Authentication failed for user: {} - {}", request.getEmail(), e.getMessage(), e);
        }
        return null;
    }

    private ResponseEntity<AuthResponse> handleAuthenticationError(String errorCode, String errorMessage) {
        if (errorCode.equals("USER_NOT_FOUND")) {
            throw new UserNotFoundException();
        }
        return null;
    }
    private CompletableFuture<Map<String, Object>> waitForProcessCompletion(long processInstanceKey) {
        return CompletableFuture.supplyAsync(() -> {
            throw new UnsupportedOperationException("Process completion monitoring not implemented");
        });
    }
}
