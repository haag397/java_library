package com.library.library.service.auth;

import com.library.library.command.AuthenticateUserCommand;
import com.library.library.command.RegisterUserCommand;
import com.library.library.dto.auth.AuthenticationRequestDTO;
import com.library.library.dto.auth.AuthenticationResponseDTO;
import com.library.library.dto.auth.RegisterRequestDTO;
import com.library.library.dto.auth.RegisterResponseDTO;
import com.library.library.exception.UserExistException;
import com.library.library.exception.UserNotFoundException;
import com.library.library.model.User;
import com.library.library.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CommandGateway commandGateway;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UsersRepository usersRepository;
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtService jwtService;

        public RegisterResponseDTO registers(RegisterRequestDTO registerRequestDTO) {
        if (usersRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new UserExistException();
        }

        if (usersRepository.existsByMobile(registerRequestDTO.getMobile())) {
            throw new UserExistException();
        }

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
                    .id(command.getUserId())
                    .email(command.getEmail())
                    .firstName(command.getFirstName())
                    .lastName(command.getLastName())
                    .mobile(command.getMobile())
                    .build();
    }

        public AuthenticationResponseDTO auth(AuthenticationRequestDTO request) {

            var user = usersRepository.findByEmail(request.getEmail())
                    .orElseThrow(UserNotFoundException::new);

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            AuthenticateUserCommand command = new AuthenticateUserCommand(
                    user.getId(),
                    user.getEmail(),
                    request.getPassword()
            );

            commandGateway.sendAndWait(command); // Fire login event

            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            return AuthenticationResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
}