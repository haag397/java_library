package com.library.library.camunda;

import com.library.library.command.AuthenticateUserCommand;
import com.library.library.exception.UserNotFoundException;
import com.library.library.repository.UsersRepository;
import com.library.library.service.auth.JwtService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticateUserWorker {

    private final UsersRepository usersRepository;
    private final AuthenticationManager authenticationManager;
    private final CommandGateway commandGateway;
    private final JwtService jwtService;

    @JobWorker(type = "authenticate")
    public void handleJob(JobClient client, ActivatedJob job) {
        Map<String, Object> variables = job.getVariablesAsMap();

        try {
            String email = (String) variables.get("email");
            String password = (String) variables.get("password");

            log.info("Processing authentication for email: {}", email);

            var user = usersRepository.findByEmail(email)
                    .orElseThrow(UserNotFoundException::new);

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            commandGateway.sendAndWait(
                    new AuthenticateUserCommand(user.getId(), email, password)
            );

            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            client.newCompleteCommand(job.getKey())
                    .variables(Map.of(
                            "status", "authenticated",
                            "accessToken", accessToken,
                            "refreshToken", refreshToken,
                            "userId", user.getId().toString(),
                            "email", email
                    ))
                    .send()
                    .join();

            log.info("Authentication successful for user: {}", email);

        } catch (UserNotFoundException e) {
            failJob(client, job, "User not found");
        } catch (Exception e) {
            failJob(client, job, "Authentication failed: " + e.getMessage());
        }
    }

    private void failJob(JobClient client, ActivatedJob job, String errorMessage) {
        client.newFailCommand(job.getKey())
                .retries(job.getRetries() - 1)
                .errorMessage(errorMessage)
                .variables(Map.of(
                        "status", "FAILED",
                        "errorMessage", errorMessage
                ))
                .send()
                .join();
    }
}