package com.library.library.command;

import com.library.library.model.Role;
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
public class UpdateUserCommand {
        @TargetAggregateIdentifier
        private UUID userId;
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private String mobile;
        private Role role;
}