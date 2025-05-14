package com.library.library.command.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;
@Data
@AllArgsConstructor
public class UserAuthenticatedEvent {
    private UUID userId;
    private String email;
    private String password;
}
