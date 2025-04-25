package com.library.library.service.auth;

import com.library.library.config.JwtService;
import com.library.library.dto.auth.AuthenticationRequestDTO;
import com.library.library.dto.auth.AuthenticationResponseDTO;
import com.library.library.dto.auth.RegisterRequestDTO;
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
        var appUser = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .mobile(request.getMobile())
                .role(request.getRole())
                .build();
            userRepository.save(appUser);
            var jwtToken = jwtService.generateToken(appUser);
            return AuthenticationResponseDTO.builder()
                    .token(jwtToken)
                    .build();
    }

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var appUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        var jwtToken = jwtService.generateToken(appUser);
        return AuthenticationResponseDTO.builder()
                .token(jwtToken)
                .build();
    }
}
