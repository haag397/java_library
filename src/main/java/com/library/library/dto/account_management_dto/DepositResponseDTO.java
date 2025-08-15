package com.library.library.dto.account_management_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositResponseDTO {

    private UUID id;
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
    private Boolean withdrawRight;
    private String branchCode;
    private String depositIban;
    private String portion;
    private Boolean isSpecial;
    private String fullName;
    private String individualOrSharedDeposit;
    private String openingDate;
    private List<SignerInfoResponseDTO> signerInfo;
    private Boolean isCommercialDeposit;
    private List<String> withdrawTools;
    private String followCode;
    private BigDecimal availableAmount;
    private BigDecimal accountAmount;
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
    public static class SignerInfoResponseDTO {
        private String customerNumber;
        private String portion;
        private String customerRelationWithDepositPersian;
        private String customerRelationWithDepositEnglish;
        private String fullName;
    }
}
