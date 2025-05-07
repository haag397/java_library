package com.library.library.service.auth;

import com.library.library.command.RegisterUserCommand;
import com.library.library.dto.auth.RegisterRequestDTO;
import com.library.library.dto.auth.RegisterResponseDTO;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CommandGateway commandGateway;
    private final PasswordEncoder passwordEncoder;

    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        UUID userId = UUID.randomUUID();

        RegisterUserCommand command = RegisterUserCommand.builder()
                .userId(userId)
                .firstName(registerRequestDTO.getFirstName())
                .lastName(registerRequestDTO.getLastName())
                .email(registerRequestDTO.getEmail())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .mobile(registerRequestDTO.getMobile())
                .role(registerRequestDTO.getRole())
                .build();

        commandGateway.sendAndWait(command);

        return RegisterResponseDTO.builder()
                .id(userId)
                .email(registerRequestDTO.getEmail())
                .firstName(registerRequestDTO.getFirstName())
                .lastName(registerRequestDTO.getLastName())
                .mobile(registerRequestDTO.getMobile())
                .build();
    }
}
