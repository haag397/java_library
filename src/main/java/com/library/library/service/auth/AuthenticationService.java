package com.library.library.service.auth;

import com.library.library.dto.auth.*;
import com.library.library.exception.InvalidRefreshTokenException;
import com.library.library.exception.InvalidTokenException;
import com.library.library.exception.UserNotFoundException;
import com.library.library.model.User;
import com.library.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenBlacklistService tokenBlacklistService;

    public RegisterResponseDTO register(RegisterRequestDTO request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .mobile(request.getMobile())
                .role(request.getRole())
                .build();
            userRepository.save(user);

            return RegisterResponseDTO.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .mobile(user.getMobile())
                    .build();
    }

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(UserNotFoundException::new);
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponseDTO refreshToken(RefreshTokenRequestDTO request) {
        String refreshToken = request.getRefreshToken();
        if (jwtService.isRefreshTokenValid(refreshToken)) {
            String userEmail = jwtService.extractUsername(refreshToken);
            var user = userRepository.findByEmail(userEmail)
                    .orElseThrow(UserNotFoundException::new);
            String newAccessToken = jwtService.generateAccessToken(user);
            return AuthenticationResponseDTO.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {
            throw new InvalidRefreshTokenException("Refresh token is invalid or expired");
        }
    }

    public LogOutResponseDTO logout(String accessToken, RefreshTokenRequestDTO refreshTokenRequest) {
        try {
            UUID userId = jwtService.extractUserId(accessToken);

            // Calculate remaining time for blacklisting
            long accessTokenTtl = jwtService.getTokenRemainingTimeInMillis(accessToken);

            tokenBlacklistService.blackListAccessToken(accessToken, accessTokenTtl);

            if (refreshTokenRequest != null && refreshTokenRequest.getRefreshToken() != null) {
                String refreshToken = refreshTokenRequest.getRefreshToken();
                long refreshTokenTtl = jwtService.getTokenRemainingTimeInMillis(refreshToken);

                // Blacklist both tokens and associate with user
                tokenBlacklistService.blackListAllUserToken(
                        userId.toString(),
                        accessToken,
                        refreshToken,
                        accessTokenTtl,
                        refreshTokenTtl
                );
            }

            return LogOutResponseDTO.builder()
                    .message("Logout successful")
                    .status(true)
                    .timestamp(LocalDateTime.now())
                    .build();
        } catch (Exception e) {
            throw new InvalidTokenException("Failed to process logout: " + e.getMessage());
        }
    }

}
