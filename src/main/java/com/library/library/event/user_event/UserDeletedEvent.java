package com.library.library.event.user_event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserDeletedEvent {
    UUID userId;
}
