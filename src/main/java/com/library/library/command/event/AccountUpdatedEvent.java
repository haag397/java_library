package com.library.library.command.event;

import com.library.library.dto.user.UpdateAccountRequestDTO;
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
    private UpdateAccountRequestDTO updatedAccountData;
}