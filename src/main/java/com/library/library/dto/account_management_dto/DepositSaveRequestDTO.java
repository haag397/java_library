package com.library.library.dto.account_management_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositSaveRequestDTO {

    private String userId;
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
    private String withdrawRight; // "true"/"false" from core
    private String branchCode;
    private String depositIban;
    private String portion;
    private String isSpecial; // "true"/"false" from core
    private String fullName;
    private String individualOrSharedDeposit;
    private String openingDate;
    private List<SignerInfoSaveDTO> signerInfo;
    private Boolean isCommercialDeposit;
    private List<String> withdrawTools;
    private String followCode;
    private String availableAmount;
    private String actualAmount;
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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SignerInfoSaveDTO {
        private String customerNumber;
        private String portion;
        private String customerRelationWithDepositPersian;
        private String customerRelationWithDepositEnglish;
        private String fullName;
    }
}
