package com.library.library.dto.auth;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record LogOutResponseDTO(
        String message,
        boolean status,
        LocalDateTime timestamp) {}
