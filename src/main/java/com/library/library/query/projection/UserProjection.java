package com.library.library.query.projection;

import com.library.library.command.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProjection {
    @EventHandler
    public void on(UserRegisteredEvent userRegisteredEvent) {

    }
}
