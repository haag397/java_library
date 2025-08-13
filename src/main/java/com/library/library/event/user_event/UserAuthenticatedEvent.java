package com.library.library.event.user_event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@AllArgsConstructor
public class UserAuthenticatedEvent {
    private UUID userId;
    private String email;
    private LocalDateTime loginTime;
}
