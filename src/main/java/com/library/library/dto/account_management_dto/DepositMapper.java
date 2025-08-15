package com.library.library.dto.account_management_dto;

import com.library.library.model.Deposit;
import com.library.library.model.SignerInfo;
import org.mapstruct.*;
import java.math.BigDecimal;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = {UUID.class, BigDecimal.class})
public interface DepositMapper {

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    @Mapping(target = "withdrawRight", expression = "java(toBoolean(dto.getWithdrawRight()))")
    @Mapping(target = "isSpecial", expression = "java(toBoolean(dto.getIsSpecial()))")
    @Mapping(target = "availableAmount", expression = "java(toBigDecimal(dto.getAvailableAmount()))")
    @Mapping(target = "accountAmount", expression = "java(toBigDecimal(dto.getActualAmount()))")
    @Mapping(target = "signerInfo", source = "signerInfo")
    Deposit toEntity(DepositSaveRequestDTO dto);

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    SignerInfo toSignerInfo(DepositSaveRequestDTO.SignerInfoSaveDTO dto);

    DepositResponseDTO toResponseDTO(Deposit entity);

    DepositResponseDTO.SignerInfoResponseDTO toSignerInfoResponseDTO(SignerInfo entity);

    // ======== Utility Conversions ========

    default Boolean toBoolean(String value) {
        return value != null && value.equalsIgnoreCase("true");
    }

    default BigDecimal toBigDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO; // fallback
        }
    }
}
