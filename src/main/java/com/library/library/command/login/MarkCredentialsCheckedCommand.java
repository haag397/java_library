package com.library.library.command.login;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record MarkCredentialsCheckedCommand(
        @TargetAggregateIdentifier
        String sessionId,
        boolean valid,
        String reason
) {}