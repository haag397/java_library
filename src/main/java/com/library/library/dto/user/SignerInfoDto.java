package com.library.library.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignerInfoDto {
    private String customerNumber;
    private String portion;
    private String customerRelationWithDepositPersian;
    private String customerRelationWithDepositEnglish;
    private String fullName;
}
