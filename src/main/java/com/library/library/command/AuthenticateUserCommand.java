package com.library.library.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticateUserCommand {
    @TargetAggregateIdentifier
    private UUID userId;
    private String email;
    private String password;
}
