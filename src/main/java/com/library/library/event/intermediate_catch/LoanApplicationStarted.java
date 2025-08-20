package com.library.library.event.intermediate_catch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationStarted {
    private String applicationId;
    private String accountId;
}