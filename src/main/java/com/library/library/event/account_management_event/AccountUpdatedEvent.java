package com.library.library.event.account_management_event;

import com.library.library.dto.account_management_dto.UpdateDepositRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdatedEvent {
    private UUID depositId;
    private UpdateDepositRequestDTO updatedAccountData;
}