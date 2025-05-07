package com.library.library.command;

import lombok.Builder;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;
@Builder
public record DeleteUserCommand(
        @TargetAggregateIdentifier
        UUID userId){}
