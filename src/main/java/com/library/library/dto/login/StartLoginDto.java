package com.library.library.dto.login;

import jakarta.validation.constraints.NotBlank;

public record StartLoginDto(
        @NotBlank String userId,
        @NotBlank String user,
        @NotBlank String pass,
        String sessionId
) {}