package com.library.library.query.projection;

import com.library.library.command.event.UserAuthenticatedEvent;
import com.library.library.command.event.UserRegisteredEvent;
import com.library.library.model.User;
import com.library.library.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@ProcessingGroup("user-projection")
public class UserProjection {
    private final UsersRepository usersRepository;

    @EventHandler
    @Transactional
    public void on(UserRegisteredEvent evt) {
       if (usersRepository.existsByEmail(evt.getEmail())) {
            log.warn("Duplicate email found: {}", evt.getEmail());
            return;
        }
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
            usersRepository.saveAndFlush(u);
        } catch (Exception ex) {
            // Likely a unique constraint on email; log and skip to avoid breaking projection
            log.warn("Duplicate user registration attempt for email: ", ex);
        }
    }

    @EventHandler
    public void on(UserAuthenticatedEvent event) {
        User user = usersRepository.findById(event.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found in projection"));

        user.setLoginTime(event.getLoginTime());
        usersRepository.save(user);
    }
}
