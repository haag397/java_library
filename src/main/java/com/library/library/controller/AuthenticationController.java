//package com.library.library.controller;
//
//import com.library.library.dto.auth.*;
//
//import com.library.library.exception.InvalidTokenException;
//import com.library.library.exception.UserExistException;
//import com.library.library.exception.UserNotFoundException;
//import com.library.library.repository.UserRepository;
//import com.library.library.service.auth.AuthenticationService;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//public class AuthenticationController {
//
//    private final AuthenticationService authenticationService;
//    private final UserRepository userRepository;
//
////    @Operation(summary = "Returns a Hello World message")
//    @PostMapping("/register")
//    public ResponseEntity<RegisterResponseDTO> register(
//            @RequestBody RegisterRequestDTO registerRequest) {
//        if (userRepository.existsByEmail(registerRequest.getEmail())){
//            throw new UserExistException();
//        }
//        return ResponseEntity.ok(authenticationService.register(registerRequest));
//    }
//
//    @PostMapping("authenticate")
//    @Operation(summary = "authenticate", description = "authenticate clients")
//    public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO authenticationRequest) {
//        if (!userRepository.existsByEmail(authenticationRequest.getEmail())){
//            throw new UserNotFoundException();
//        }
//        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
//    }
//
//    @PostMapping("/refresh-token")
//    public ResponseEntity<AuthenticationResponseDTO> refreshToken(
//            @RequestBody RefreshTokenRequestDTO request
//    ) {
//        return ResponseEntity.ok(authenticationService.refreshToken(request));
//    }
//
//    @PostMapping("/logout")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<LogOutResponseDTO> logout(
//            @RequestHeader("Authorization") String authHeader,
//            @RequestBody(required = false) RefreshTokenRequestDTO refreshRequestDTO) {
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new InvalidTokenException("Invalid access token format");
//        }
//
//        String accessToken = authHeader.substring(7);
//        LogOutResponseDTO response = authenticationService.logout(accessToken, refreshRequestDTO);
//
//        // Clear security context
//        SecurityContextHolder.clearContext();
//
//        return ResponseEntity.ok(response);
//    }
//    }
//
//
