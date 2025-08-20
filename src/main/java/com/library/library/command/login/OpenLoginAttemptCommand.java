package com.library.library.command.login;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record OpenLoginAttemptCommand(
        @TargetAggregateIdentifier
        String sessionId,
        String userId,
        String user
) {}