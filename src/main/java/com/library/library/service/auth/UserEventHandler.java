package com.library.library.service.auth;

import com.library.library.command.event.UserDeletedEvent;
import com.library.library.command.event.UserRegisteredEvent;
import com.library.library.command.event.UserUpdatedEvent;

public interface UserEventHandler {
    public void on(UserRegisteredEvent event);
    public void on(UserUpdatedEvent event);
    public void on(UserDeletedEvent event);
}
