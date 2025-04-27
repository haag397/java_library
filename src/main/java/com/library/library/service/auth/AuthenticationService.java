package com.library.library.service.auth;

import com.library.library.config.JwtService;
import com.library.library.dto.auth.AuthenticationRequestDTO;
import com.library.library.dto.auth.AuthenticationResponseDTO;
import com.library.library.dto.auth.RefreshTokenRequestDTO;
import com.library.library.dto.auth.RegisterRequestDTO;
import com.library.library.exception.InvalidRefreshTokenException;
import com.library.library.exception.UserNotFoundException;
import com.library.library.model.User;
import com.library.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDTO register(RegisterRequestDTO request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .mobile(request.getMobile())
                .role(request.getRole())
                .build();
            userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
            return AuthenticationResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
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
                .orElseThrow(() -> new UserNotFoundException("User not found"));
//        System.out.println("User email extracted: " + userEmail);
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
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            String newAccessToken = jwtService.generateAccessToken(user);
            return AuthenticationResponseDTO.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken) // usually, the refresh token stays the same
                    .build();
        } else {
            throw new InvalidRefreshTokenException("Refresh token is invalid or expired");
        }
    }

}
