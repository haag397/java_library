package com.library.library.command.login;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record SubmitOtpCommand(
        @TargetAggregateIdentifier String sessionId,
        String otp
) {}