package com.library.library.command;

import com.library.library.model.Role;
import lombok.Builder;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;
@Builder
public record UpdateUserCommand(
        @TargetAggregateIdentifier
        UUID userId,
        String firstName,
        String lastName,
        String email,
        String password,
        String mobile,
        Role role){}
