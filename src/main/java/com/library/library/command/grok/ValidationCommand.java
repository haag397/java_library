package com.library.library.command.grok;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationCommand {
    @TargetAggregateIdentifier
    private String processId;
    private String data;
}