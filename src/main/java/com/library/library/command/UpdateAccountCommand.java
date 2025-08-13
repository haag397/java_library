package com.library.library.command;

import com.library.library.dto.user.UpdateAccountRequestDTO;
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
    private UpdateAccountRequestDTO updateAccountRequest;
}
