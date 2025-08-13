package com.library.library.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RejectLoanCommand {
    @TargetAggregateIdentifier
    private String loanId;
    private String reason;
}