package com.library.library.controller;

import com.library.library.dto.auth.AuthenticationRequestDTO;
import com.library.library.dto.auth.AuthenticationResponseDTO;
import com.library.library.dto.auth.RegisterRequestDTO;
import com.library.library.dto.auth.RegisterResponseDTO;
import com.library.library.exception.UserNotFoundException;
import com.library.library.repository.UsersRepository;
import com.library.library.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/event/")
@RequiredArgsConstructor
public class AuthController {

    private final UsersRepository usersRepository;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
        return ResponseEntity.ok(authService.registers(registerRequestDTO));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticates(@RequestBody AuthenticationRequestDTO dto) {
        if (!usersRepository.existsByEmail(dto.getEmail())){
            throw new UserNotFoundException();
        }
        return ResponseEntity.ok(authService.auth(dto));
    }
}