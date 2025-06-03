package com.library.library.dto.auth;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AuthResponse(
    String accessToken,
    String refreshToken,
    String email,
    UUID userId){}