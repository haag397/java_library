package com.library.library.aggregate.intermediate_catch;

import com.library.library.command.intermediate_catch.*;
import com.library.library.event.intermediate_catch.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateCreationPolicy;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.CreationPolicy;
import org.axonframework.spring.stereotype.Aggregate;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class LoanApplication {

    @AggregateIdentifier
    private String applicationId;

    private String accountId;
    private boolean started;        // guard
    private boolean accountValid;
    private boolean userDataProvided;
    private boolean granted;

    protected LoanApplication() {}

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    public void handle(StartLoanApplication cmd) {
        if (cmd.applicationId() == null || cmd.accountId() == null) {
            throw new IllegalArgumentException("Missing ids");
        }
        if (started) {
            // idempotent: already started → do nothing
            return;
        }
        apply(new LoanApplicationStarted(cmd.applicationId(), cmd.accountId()));
    }

    @CommandHandler
    public void handle(ValidateAccount cmd) {
        boolean valid = cmd.accountId() != null && cmd.accountId().startsWith("ACC-");
        if (!valid) throw new BusinessRuleViolation("ACCOUNT_INVALID");
        apply(new AccountValidated(cmd.applicationId()));
    }

    @CommandHandler public void handle(MarkValidationRequested cmd) { apply(new ValidationRequested(cmd.applicationId())); }
    @CommandHandler public void handle(ProvideUserData cmd) {
        if (cmd.payload() == null) throw new BusinessRuleViolation("USER_DATA_MISSING");
        apply(new UserDataProvided(cmd.applicationId()));
    }
    @CommandHandler public void handle(GrantLoan cmd) {
        if (!accountValid) throw new BusinessRuleViolation("ACCOUNT_INVALID");
        if (!userDataProvided) throw new BusinessRuleViolation("VALIDATION_FAILED");
        if (!granted) apply(new LoanGranted(cmd.applicationId()));
    }

    @EventSourcingHandler
    public void on(LoanApplicationStarted e) {
        this.applicationId = e.getApplicationId();
        this.accountId = e.getAccountId();
        this.started = true;
    }
    @EventSourcingHandler public void on(AccountValidated e){ this.accountValid = true; }
    @EventSourcingHandler public void on(UserDataProvided e){ this.userDataProvided = true; }
    @EventSourcingHandler public void on(LoanGranted e){ this.granted = true; }

    public static class BusinessRuleViolation extends RuntimeException {
        public BusinessRuleViolation(String code){ super(code); }
    }
}