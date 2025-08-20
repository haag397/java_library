package com.library.library.command.intermediate_catch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
public record ValidateAccount(@TargetAggregateIdentifier String applicationId, String accountId) {}
