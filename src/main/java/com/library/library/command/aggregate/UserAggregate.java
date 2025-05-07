package com.library.library.command.aggregate;

import com.library.library.command.DeleteUserCommand;
import com.library.library.command.RegisterUserCommand;
import com.library.library.command.UpdateUserCommand;
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

    private boolean deleted = false;

    public UserAggregate() {}

    @CommandHandler
    public UserAggregate(RegisterUserCommand registerUserCommand) {
        apply(new UserRegisteredEvent(
                registerUserCommand.userId(),
                registerUserCommand.firstName(),
                registerUserCommand.lastName(),
                registerUserCommand.email(),
                registerUserCommand.password(),
                registerUserCommand.mobile(),
                registerUserCommand.role()
        ));
    }

    @EventSourcingHandler
    public void on(UserRegisteredEvent userRegisteredEvent) {
        this.userId = userRegisteredEvent.getUserId();
        this.email = userRegisteredEvent.getEmail();
        this.firstName = userRegisteredEvent.getFirstName();
        this.lastName = userRegisteredEvent.getLastName();
        this.role = userRegisteredEvent.getRole();
        this.mobile = userRegisteredEvent.getMobile();
    }

    @CommandHandler
    public void handle(UpdateUserCommand updateUserCommand) {
        apply(new UserUpdatedEvent(
                updateUserCommand.userId(),
                updateUserCommand.firstName(),
                updateUserCommand.lastName(),
                updateUserCommand.password(),
                updateUserCommand.email(),
                updateUserCommand.mobile(),
                updateUserCommand.role()
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
        apply(new UserDeletedEvent(deleteUserCommand.userId()));
    }

    @EventSourcingHandler
    public void on(UserDeletedEvent event) {
        this.deleted = true;
    }
}
