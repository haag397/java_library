package com.library.library.controller;

import com.library.library.dto.auth.*;
import com.library.library.exception.UserExistException;
import com.library.library.exception.UserNotFoundException;
import com.library.library.repository.UsersRepository;
import com.library.library.service.auth.AuthService;
import io.camunda.zeebe.client.api.response.ProcessInstanceResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import io.camunda.zeebe.client.ZeebeClient;

@RestController
@RequestMapping("/api/event/")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UsersRepository usersRepository;
    private final AuthService authService;

    private final ZeebeClient zeebeClient;

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
    public ResponseEntity<Map<String, Object>>  startRegistration(@RequestBody RegisterRequestDTO dto) {
        if (usersRepository.existsByEmail(dto.getEmail()) || usersRepository.existsByMobile(dto.getMobile())) {
            throw new UserExistException();
        }

        Map<String, Object> variables = Map.of(
                "firstName", dto.getFirstName(),
                "lastName", dto.getLastName(),
                "email", dto.getEmail(),
                "mobile", dto.getMobile(),
                "password", dto.getPassword(),
                "role", dto.getRole().name()
        );
        ProcessInstanceResult result = zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId("user-register")
                .latestVersion()
                .variables(variables)
                .withResult() // <--- Wait for process completion
                .send()
                .join();

        String status = (String) result.getVariablesAsMap().get("status");

        if (!"completed".equals(status)) {
            String errorCode = (String) result.getVariablesAsMap().get("errorCode");
            String message = (String) result.getVariablesAsMap().get("message");
            throw new UserExistException(); }

        // Extract user info to return
        Map<String, Object> response = Map.of(
                "userId", result.getVariablesAsMap().get("userId"),
                "email", result.getVariablesAsMap().get("email"),
                "firstName", result.getVariablesAsMap().get("firstName"),
                "lastName", result.getVariablesAsMap().get("lastName")
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/start-authentication")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthenticationRequestDTO dto) {
        Map<String, Object> variables = Map.of(
                "email", dto.getEmail(),
                "password", dto.getPassword()
        );

        ProcessInstanceResult result = zeebeClient
                .newCreateInstanceCommand()
                .bpmnProcessId("authentication-process")  // BPMN ID of your auth process
                .latestVersion()
                .variables(variables)
                .withResult()  // <--- This waits for process completion
                .send()
                .join();

        String status = (String) result.getVariablesAsMap().get("status");
        if (!"authenticated".equals(status)) {
            throw new UserNotFoundException();
        }

        String accessToken = (String) result.getVariablesAsMap().get("accessToken");
        String refreshToken = (String) result.getVariablesAsMap().get("refreshToken");

        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        ));
    }
}
