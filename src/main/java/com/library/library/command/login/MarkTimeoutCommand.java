package com.library.library.command.login;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record MarkTimeoutCommand(
        @TargetAggregateIdentifier String sessionId
) {}