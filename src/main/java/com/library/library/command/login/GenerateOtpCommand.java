package com.library.library.command.login;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.Instant;

public record GenerateOtpCommand(
        @TargetAggregateIdentifier
        String sessionId,
        String otpHash,
        Instant expiresAt
) {}