package com.library.library.dto.account;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositResponseDto {

    private String depositNumber;
    private String mainCustomerNumber;
    private String depositTitle;

    private Boolean withdrawRight;  // Use Boolean, not String

    private String branchCode;

    // other fields...

    // getters and setters
}