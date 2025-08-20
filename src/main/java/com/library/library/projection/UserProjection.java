package com.library.library.projection;

import com.library.library.event.user_event.UserAuthenticatedEvent;
import com.library.library.event.user_event.UserRegisteredEvent;
import com.library.library.exception.UserExistException;
import com.library.library.model.User;
import com.library.library.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
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
            log.warn("Skipping duplicate projection for user with email: {}", evt.getEmail());
            return;
        }

        try {
            User u = User.builder()
                    .id(evt.getUserId())
                    .firstName(evt.getFirstName())
                    .lastName(evt.getLastName())
                    .email(evt.getEmail())
                    .password(evt.getPassword())
                    .mobile(evt.getMobile())
                    .role(evt.getRole())
                    .build();
            usersRepository.saveAndFlush(u);
        } catch (Exception ex) {
            // Likely a unique constraint on email; log and skip to avoid breaking projection
            log.error("Projection save failed: {}", ex.getMessage());        }
    }

    @EventHandler
    public void on(UserAuthenticatedEvent event) {
        User user = usersRepository.findById(event.getUserId())
                .orElseThrow(UserExistException::new);

        user.setLoginTime(event.getLoginTime());
        usersRepository.save(user);
    }
}
