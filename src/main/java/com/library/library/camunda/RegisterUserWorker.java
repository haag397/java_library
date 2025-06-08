package com.library.library.camunda;

import com.library.library.command.RegisterUserCommand;
import com.library.library.dto.auth.RegisterResponseDTO;
import com.library.library.exception.UserExistException;
import com.library.library.model.Role;
import com.library.library.repository.UsersRepository;
import com.library.library.service.auth.AuthService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.Variable;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.library.library.dto.auth.RegisterRequestDTO;

import io.camunda.zeebe.spring.client.annotation.JobWorker;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegisterUserWorker {

    private final CommandGateway commandGateway;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final UsersRepository usersRepository;

    @JobWorker(type = "registration")
    public void handleJob(
            JobClient client,
            ActivatedJob job,
            @Variable String firstName,
            @Variable String lastName,
            @Variable String email,
            @Variable String mobile,
            @Variable String password,
            @Variable String role
    ) {
        try {

            if (usersRepository.existsByEmail(email) || usersRepository.existsByMobile(mobile)) {
                client.newCompleteCommand(job.getKey())
                        .variables(Map.of(
                                "status", "error",
                                "errorCode", "USER_ALREADY_EXISTS",
                                "message", "User with this email already exists."
                        ))
                        .send()
                        .join();
                throw new UserExistException();
            }

            UUID userId = UUID.randomUUID();
            RegisterUserCommand command = RegisterUserCommand.builder()
                    .userId(userId)
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .mobile(mobile)
                    .role(Role.valueOf(role)) // Assuming enum
                    .build();

            commandGateway.send(command);

            client.newCompleteCommand(job.getKey())
                    .variables(Map.of(
                            "status", "completed",
                            "userId", userId.toString()
                    ))
                    .send()
                    .join();

        } catch (Exception e) {
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage("Failed to register user: " + e.getMessage())
                    .send()
                    .join();
        }
    }
}

