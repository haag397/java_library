package com.library.library.command.login;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record MarkOtpCheckedCommand(
        @TargetAggregateIdentifier String sessionId,
        boolean valid, int attempts, String reason
) {}