package com.library.library.aggregate.login;

import com.library.library.command.login.*;
import com.library.library.event.login.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.time.Instant;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class LoginAttemptAggregate {

    @AggregateIdentifier
    private String sessionId;

    private String userId;
    private String user;

    private String otpHash;
    private Instant expiresAt;
    private int attempts;
    private int maxAttempts = 3;

    private String state; // STARTED, OTP_SENT, SUCCESS, FAILED

    public LoginAttemptAggregate() {}

    @CommandHandler
    public LoginAttemptAggregate(OpenLoginAttemptCommand cmd) {
        apply(new LoginAttemptOpenedEvent(cmd.sessionId(), cmd.userId(), cmd.user()));
    }

    @EventSourcingHandler
    public void on(LoginAttemptOpenedEvent e) {
        this.sessionId = e.sessionId();
        this.userId = e.userId();
        this.user = e.user();
        this.state = "STARTED";
        this.attempts = 0;
    }

    @CommandHandler
    public void handle(MarkCredentialsCheckedCommand cmd) {
        apply(new CredentialsCheckedEvent(cmd.sessionId(), cmd.valid(), cmd.reason()));
    }

    @EventSourcingHandler
    public void on(CredentialsCheckedEvent e) {
        // no-op; می‌توان reason را ذخیره کرد
    }

    @CommandHandler
    public void handle(GenerateOtpCommand cmd) {
        apply(new OtpGeneratedEvent(cmd.sessionId(), cmd.otpHash(), cmd.expiresAt()));
    }

    @EventSourcingHandler
    public void on(OtpGeneratedEvent e) {
        this.otpHash = e.otpHash();
        this.expiresAt = e.expiresAt();
        this.attempts = this.attempts; // بدون تغییر
        this.state = "OTP_SENT";
    }

    @CommandHandler
    public void handle(RecordOtpSentCommand cmd) {
        apply(new OtpSentEvent(cmd.sessionId(), cmd.channel()));
    }

    @EventSourcingHandler
    public void on(OtpSentEvent e) { /* audit */ }

    @CommandHandler
    public void handle(SubmitOtpCommand cmd) {
        apply(new OtpSubmittedEvent(cmd.sessionId()));
    }

    @EventSourcingHandler
    public void on(OtpSubmittedEvent e) { /* audit */ }

    @CommandHandler
    public void handle(MarkOtpCheckedCommand cmd) {
        apply(new OtpCheckedEvent(cmd.sessionId(), cmd.valid(), cmd.attempts(), cmd.reason()));
    }

    @EventSourcingHandler
    public void on(OtpCheckedEvent e) {
        this.attempts = e.attempts();
        if (e.valid()) {
            this.state = "SUCCESS";
        } else if (this.attempts >= this.maxAttempts) {
            this.state = "FAILED";
        }
    }

    @CommandHandler
    public void handle(MarkTimeoutCommand cmd) {
        apply(new OtpTimeoutEvent(cmd.sessionId()));
    }

    @EventSourcingHandler
    public void on(OtpTimeoutEvent e) {
        this.state = "FAILED";
    }

    @CommandHandler
    public void handle(MarkFinishedCommand cmd) {
        apply(new LoginFinishedEvent(cmd.sessionId(), cmd.success(), cmd.reason()));
    }

    @EventSourcingHandler
    public void on(LoginFinishedEvent e) {
        this.state = e.success() ? "SUCCESS" : "FAILED";
    }
}