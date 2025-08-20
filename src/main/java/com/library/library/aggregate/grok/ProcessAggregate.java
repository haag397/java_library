package com.library.library.aggregate.grok;

import com.library.library.command.grok.CheckingCommand;
import com.library.library.command.grok.ValidationCommand;
import com.library.library.event.grok.CheckingFailedEvent;
import com.library.library.event.grok.CheckingSucceededEvent;
import com.library.library.event.grok.ValidationFailedEvent;
import com.library.library.event.grok.ValidationSucceededEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class ProcessAggregate {

    @AggregateIdentifier
    private String processId;
    private String status;

    /** الزامی برای Axon */
    protected ProcessAggregate() { }

    /** Creation Handler: فقط همین یک هندلر برای ValidationCommand */
    @CommandHandler
    public ProcessAggregate(ValidationCommand cmd) {
        if (cmd.getData() == null || cmd.getData().isEmpty()) {
            AggregateLifecycle.apply(new ValidationFailedEvent(cmd.getProcessId(), "Validation failed: empty data"));
            throw new IllegalArgumentException("Validation failed: empty data");
        }
        AggregateLifecycle.apply(new ValidationSucceededEvent(cmd.getProcessId(), "Validation success"));
    }

    /** Update Handler: CheckingCommand روی aggregate موجود اجرا می‌شود */
    @CommandHandler
    public void handle(CheckingCommand cmd) {
        if (cmd.getData() == null || cmd.getData().isEmpty()) {
            AggregateLifecycle.apply(new CheckingFailedEvent(cmd.getProcessId(), "Checking failed: empty data"));
            throw new IllegalArgumentException("Checking failed: empty data");
        }
        AggregateLifecycle.apply(new CheckingSucceededEvent(cmd.getProcessId(), "Checking success"));
    }

    /** Event Sourcing: مقداردهی شناسه و وضعیت از روی رویدادها */
    @EventSourcingHandler
    public void on(ValidationSucceededEvent e) {
        this.processId = e.getProcessId();
        this.status = "validated";
    }

    @EventSourcingHandler
    public void on(ValidationFailedEvent e) {
        this.processId = e.getProcessId();
        this.status = "validation_failed";
    }

    @EventSourcingHandler
    public void on(CheckingSucceededEvent e) {
        this.processId = e.getProcessId();
        this.status = "checked";
    }

    @EventSourcingHandler
    public void on(CheckingFailedEvent e) {
        this.processId = e.getProcessId();
        this.status = "checking_failed";
    }
}