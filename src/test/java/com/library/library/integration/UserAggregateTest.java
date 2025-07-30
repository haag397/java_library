//package com.library.library.integration;
//
//import com.library.library.command.RegisterUserCommand;
//import com.library.library.command.aggregate.UserAggregate;
//import com.library.library.command.event.UserRegisteredEvent;
//import com.library.library.model.Role;
//import org.axonframework.test.aggregate.AggregateTestFixture;
//import org.axonframework.test.aggregate.FixtureConfiguration;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.UUID;
//
//public class UserAggregateTest {
//
//    private FixtureConfiguration<UserAggregate> fixture;
//
//    @BeforeEach
//    public void setUp() {
//        fixture = new AggregateTestFixture<>(UserAggregate.class);
//    }
//
//    @Test
//    public void shouldRegisterUserSuccessfully() {
//        UUID userId = UUID.randomUUID();
//
//        RegisterUserCommand command = new RegisterUserCommand(
//                userId,
//                "John",
//                "Doe",
//                "johndoe@mail.com",
//                "password",
//                "02135698523",
//                Role.USER
//        );
//
//        UserRegisteredEvent expectedEvent = new UserRegisteredEvent(
//                userId,
//                "John",
//                "Doe",
//                "johndoe@mail.com",
//                "password",
//                "02135698523",
//                Role.USER
//        );
//
//        fixture.givenNoPriorActivity()
//                .when(command)
//                .expectSuccessfulHandlerExecution()
//                .expectEvents(expectedEvent);
//    }
//}
