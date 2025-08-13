package com.library.library.event.user_event;

import com.library.library.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserRegisteredEvent {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String mobile;
    private Role role;
}
