package com.library.library.event.login;

import java.time.Instant;

public record OtpGeneratedEvent(
        String sessionId,
        String otpHash,
        Instant expiresAt) {}