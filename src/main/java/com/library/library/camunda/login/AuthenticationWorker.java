package com.library.library.camunda.login;

import com.library.library.command.AuthenticateUserCommand;
import com.library.library.controller.login.OtpService;
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
public class AuthenticationWorker {
    private final UsersRepository usersRepository;
    private final AuthenticationManager authenticationManager;
    private final CommandGateway commandGateway;
    private final JwtService jwtService;
    private final OtpService otpService;

    @JobWorker(type = "authentication")
    public void handleJob(JobClient client, ActivatedJob job) {
        Map<String, Object> variables = job.getVariablesAsMap();
        String email = (String) variables.get("email");
        String password = (String) variables.get("password");
        log.info("Processing authentication for email: {}", email);
        try {
            var user = usersRepository.findByEmail(email)
                    .orElseThrow(UserNotFoundException::new);
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            commandGateway.sendAndWait(
                    new AuthenticateUserCommand(user.getId(), email, password)
            );
            Map<String, Object> vars = Map.of(
                    "isValid", true,
                    "status", "authenticated",
                    "accessToken", accessToken,
                    "refreshToken", refreshToken,
                    "userId", user.getId().toString(),
                    "email", email,
                    "authenticationStatus", "SUCCESS"
            );
            client.newCompleteCommand(job.getKey())
                    .variables(vars)
                    .send()
                    .join();
            otpService.updateTaskStatus(job.getProcessInstanceKey(), "authenticationStatus", "SUCCESS");
            log.info("Authentication successful for user: {}, set authenticationStatus=SUCCESS", email);
        } catch (Exception e) {
            Map<String, Object> vars = Map.of(
                    "isValid", false,
                    "status", "FAILED",
                    "errorMessage", e.getMessage(),
                    "authenticationStatus", "FAILED"
            );
            client.newCompleteCommand(job.getKey())
                    .variables(vars)
                    .send()
                    .join();
            otpService.updateTaskStatus(job.getProcessInstanceKey(), "authenticationStatus", "FAILED");
            log.info("Authentication failed for user: {}, set authenticationStatus=FAILED", email);
        }
    }
}
