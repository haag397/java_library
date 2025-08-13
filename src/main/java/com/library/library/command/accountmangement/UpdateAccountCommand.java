package com.library.library.command.accountmangement;

import com.library.library.dto.account_management_dto.UpdateDepositRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountCommand {
    @TargetAggregateIdentifier
    private UUID depositId;
    private UpdateDepositRequestDTO updateAccountRequest;
}
