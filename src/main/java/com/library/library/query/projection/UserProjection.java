package com.library.library.query.projection;

import com.library.library.command.event.UserRegisteredEvent;
import com.library.library.exception.UserExistException;
import com.library.library.model.User;
import com.library.library.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.dao.DataIntegrityViolationException;

@RequiredArgsConstructor
public class UserProjection {
    private final UsersRepository usersRepository;

    @EventHandler
    public void on(UserRegisteredEvent evt) {
        User u = User.builder()
                .id(evt.getUserId())
                .firstName(evt.getFirstName())
                .lastName(evt.getLastName())
                .email(evt.getEmail())
                .password(evt.getPassword())
                .mobile(evt.getMobile())
                .role(evt.getRole())
                .build();
        try {
            usersRepository.save(u);
        } catch (DataIntegrityViolationException ex) {
            // wrap in a custom exception so it can be handled cleanly
            throw new UserExistException();
        }
    }
}
