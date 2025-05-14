package com.library.library.command.aggregate;

import com.library.library.command.AuthenticateUserCommand;
import com.library.library.command.DeleteUserCommand;
import com.library.library.command.RegisterUserCommand;
import com.library.library.command.UpdateUserCommand;
import com.library.library.command.event.UserAuthenticatedEvent;
import com.library.library.command.event.UserDeletedEvent;
import com.library.library.command.event.UserRegisteredEvent;
import com.library.library.command.event.UserUpdatedEvent;
import com.library.library.model.Role;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class UserAggregate {
    @AggregateIdentifier
    private UUID userId;
    private String email;
    private String firstName;
    private Role role;
    private String mobile;
    private String lastName;
    private String password;

    private boolean deleted = false;

    public UserAggregate() {}

    @CommandHandler
    public UserAggregate(RegisterUserCommand registerUserCommand) {
        apply(new UserRegisteredEvent(
                registerUserCommand.getUserId(),
                registerUserCommand.getFirstName(),
                registerUserCommand.getLastName(),
                registerUserCommand.getEmail(),
                registerUserCommand.getPassword(),
                registerUserCommand.getMobile(),
                registerUserCommand.getRole()
        ));
    }

    @EventSourcingHandler
    public void on(UserRegisteredEvent userRegisteredEvent) {
        this.userId = userRegisteredEvent.getUserId();
        this.firstName = userRegisteredEvent.getFirstName();
        this.lastName = userRegisteredEvent.getLastName();
        this.email = userRegisteredEvent.getEmail();
        this.password = userRegisteredEvent.getPassword();
        this.mobile = userRegisteredEvent.getMobile();
        this.role = userRegisteredEvent.getRole();
    }

    @CommandHandler
    public void handle(AuthenticateUserCommand authenticateUserCommand) {
        apply(new UserAuthenticatedEvent(
                authenticateUserCommand.getUserId(),
                authenticateUserCommand.getEmail(),
                authenticateUserCommand.getPassword()
        ));
    }

    @EventSourcingHandler
    public void on(UserAuthenticatedEvent userAuthenticatedEvent) {
        this.userId = userAuthenticatedEvent.getUserId();
        this.email = userAuthenticatedEvent.getEmail();
        this.password = userAuthenticatedEvent.getPassword();
    }

    @CommandHandler
    public void handle(UpdateUserCommand updateUserCommand) {
        apply(new UserUpdatedEvent(
                updateUserCommand.getUserId(),
                updateUserCommand.getFirstName(),
                updateUserCommand.getLastName(),
                updateUserCommand.getEmail(),
                updateUserCommand.getPassword(),
                updateUserCommand.getMobile(),
                updateUserCommand.getRole()
        ));
    }

    @EventSourcingHandler
    public void on(UserUpdatedEvent event) {
        this.firstName = event.getFirstName();
        this.lastName = event.getLastName();
        this.email = event.getEmail();
        this.mobile = event.getMobile();
        this.role = event.getRole();
    }

    @CommandHandler
    public void handle(DeleteUserCommand deleteUserCommand) {
        if (deleted) {
            throw new IllegalStateException("User already deleted");
        }
        apply(new UserDeletedEvent(deleteUserCommand.getUserId()));
    }

    @EventSourcingHandler
    public void on(UserDeletedEvent event) {
        this.deleted = true;
    }
}
