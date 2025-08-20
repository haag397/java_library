package com.library.library.dto.login;

import jakarta.validation.constraints.NotBlank;

public record SubmitOtpDto(
        @NotBlank String sessionId,
        @NotBlank String otp
) {}