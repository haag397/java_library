package com.library.library.command.login;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record RecordOtpSentCommand(
        @TargetAggregateIdentifier
        String sessionId,
        String channel
) {}