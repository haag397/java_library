package com.library.library.command.event;

import com.library.library.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserRegisteredEvent {
    private UUID userId;
    private String email;
    private String lastName;
    private String firstName;
    private String password;
    private String mobile;
    private Role role;

}
