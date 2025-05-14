package com.library.library.service.auth;

import com.library.library.command.event.UserDeletedEvent;
import com.library.library.command.event.UserRegisteredEvent;
import com.library.library.command.event.UserUpdatedEvent;
import com.library.library.exception.UserNotFoundException;
import com.library.library.model.User;
import com.library.library.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@ProcessingGroup("user")
public class UserEventHandlerImpl implements UserEventHandler {

    private final UsersRepository usersRepository;

    @EventHandler
    @Override
    public void on(UserRegisteredEvent event) {
        User user = new User();
        user.setId(event.getUserId());
        user.setFirstName(event.getFirstName());
        user.setLastName(event.getLastName());
        user.setEmail(event.getEmail());
        user.setPassword(event.getPassword());
        user.setMobile(event.getMobile());
        user.setRole(event.getRole());

        usersRepository.save(user);
    }


    @EventHandler
    @Override
    public void on(UserUpdatedEvent event) {
        Optional<User> optionalUser = usersRepository.findById(event.getUserId());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFirstName(event.getFirstName());
            user.setLastName(event.getLastName());
            user.setEmail(event.getEmail());
            user.setPassword(event.getPassword());
            user.setMobile(event.getMobile());
            user.setRole(event.getRole());

            usersRepository.save(user);
        } else {
            throw new UserNotFoundException();
        }
    }

    @EventHandler
    @Override
    public void on(UserDeletedEvent event) {
        User user = usersRepository.findById(event.getUserId())
                .orElseThrow(UserNotFoundException::new);

        user.setDeleted(true);
        usersRepository.save(user);
    }
}