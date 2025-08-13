package com.library.library.dto.account_management_dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDepositRequestDTO {
    private String depositNumber;
    private String mainCustomerNumber;
    private String depositTitle;
    private String depositTypeNumber;
    private String depositTypeTitle;
    private String customerRelationWithDepositPersian;
    private String customerRelationWithDepositEnglish;
    private String depositState;
    private String currencyName;
    private String currencySwiftCode;
    private Boolean withdrawRight;
    private String branchCode;
    private String depositIban;
    private String portion;
    private Boolean isSpecial;
    private String fullName;
    private String individualOrSharedDeposit;
    private String openingDate;
    private List<SignerInfoDto> signerInfo;
    private Boolean isCommercialDeposit;
    private List<String> withdrawTools;
    private String followCode;
    private BigDecimal availableAmount;
    private BigDecimal actualAmount;
    private Boolean currentWithoutChequeBook;
    private List<String> withdrawToolCodes;
    private Boolean depositRight;
    private String englishIndividualOrSharedDeposit;
    private String depositIdentity;
    private String depositIdentityCode;
    private String depositInterestRateInfo;
    private Boolean withdrawRightWithCheque;
    private String depositTypeTreeRoot;
    private String depositTypeTreeRootCode;
}
