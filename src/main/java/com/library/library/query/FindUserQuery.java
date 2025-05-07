package com.library.library.query;

import lombok.Builder;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

public record FindUserQuery(
        @TargetAggregateIdentifier
        UUID userId){}
