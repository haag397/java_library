package com.library.library.command.login;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record MarkFinishedCommand(
        @TargetAggregateIdentifier String sessionId,
        boolean success, String reason
) {}