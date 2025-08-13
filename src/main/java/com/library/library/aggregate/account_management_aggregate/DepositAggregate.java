package com.library.library.aggregate.account_management_aggregate;

import com.library.library.command.accountmangement.UpdateAccountCommand;
import com.library.library.event.account_management_event.AccountUpdatedEvent;
import com.library.library.dto.account_management_dto.SignerInfoDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Aggregate
@Data
@NoArgsConstructor
public class DepositAggregate {

    @AggregateIdentifier
    private UUID depositId;
    private String depositNumber;
    private String mainCustomerNumber;
    private BigDecimal availableAmount;
    private List<SignerInfoDto> signerInfo = new ArrayList<>();

    // Command Handler method to process the UpdateAccountCommand
    // This is where the business logic and validation should be.
    // In a real scenario, you'd check for business rules here before applying the event.
    @CommandHandler
    public DepositAggregate(UpdateAccountCommand command) {
        // Validation logic can go here (e.g., check if the user is authorized)
        System.out.println("Processing UpdateAccountCommand for ID: " + command.getDepositId());

        // Publish the event to trigger state change and update the read model.
        // The event contains the new state of the account.
        AggregateLifecycle.apply(AccountUpdatedEvent.builder()
                .depositId(command.getDepositId())
                .updatedAccountData(command.getUpdateAccountRequest())
                .build());
    }

    // Event Sourcing Handler method to update the aggregate's state
    // This method is used to rebuild the aggregate's state from the event stream.
    @EventSourcingHandler
    public void on(AccountUpdatedEvent event) {
        System.out.println("Applying AccountUpdatedEvent for ID: " + event.getDepositId());

        // Update the aggregate's state with the data from the event
        this.depositId = event.getDepositId();
        this.depositNumber = event.getUpdatedAccountData().getDepositNumber();
        this.mainCustomerNumber = event.getUpdatedAccountData().getMainCustomerNumber();
        this.availableAmount = event.getUpdatedAccountData().getAvailableAmount();
        this.signerInfo = event.getUpdatedAccountData().getSignerInfo();

        // Other fields would be updated here as well
    }
}