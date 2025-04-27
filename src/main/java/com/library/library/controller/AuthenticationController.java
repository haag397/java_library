package com.library.library.controller;

import com.library.library.config.JwtService;
import com.library.library.dto.auth.AuthenticationRequestDTO;
import com.library.library.dto.auth.RefreshTokenRequestDTO;
import com.library.library.repository.UserRepository;
import com.library.library.service.auth.AuthenticationService;
import com.library.library.dto.auth.AuthenticationResponseDTO;
import com.library.library.dto.auth.RegisterRequestDTO;
import com.library.library.service.auth.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(
            @RequestBody RegisterRequestDTO registerRequest) {
        System.out.println("=============================================");
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    @PostMapping("authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO authenticationRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponseDTO> refreshToken(
            @RequestBody RefreshTokenRequestDTO request
    ) {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }

    @GetMapping("/a")
    public String hi() {
        return "admin";
    }

    }


