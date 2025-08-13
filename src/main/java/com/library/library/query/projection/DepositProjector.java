package com.library.library.query.projection;

import com.library.library.command.event.AccountUpdatedEvent;
import com.library.library.dto.user.UpdateAccountRequestDTO;
import com.library.library.model.SignerInfo;
import com.library.library.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@ProcessingGroup("deposit-projector")
@RequiredArgsConstructor
public class DepositProjector {

    private final DepositRepository depositRepository;

    // This method listens to AccountUpdatedEvent and updates the read model
    @EventHandler
    public void on(AccountUpdatedEvent event) {
        System.out.println("Updating read model for deposit: " + event.getDepositId());

        // Find the existing read model entity
        depositRepository.findById(event.getDepositId()).ifPresent(deposit -> {

            // Map the DTO data from the event to the JPA entity
            UpdateAccountRequestDTO dto = event.getUpdatedAccountData();
            deposit.setDepositNumber(dto.getDepositNumber());
            deposit.setMainCustomerNumber(dto.getMainCustomerNumber());
            deposit.setDepositTitle(dto.getDepositTitle());
            deposit.setDepositTypeNumber(dto.getDepositTypeNumber());
            deposit.setDepositTypeTitle(dto.getDepositTypeTitle());
            deposit.setDepositState(dto.getDepositState());
            deposit.setAvailableAmount(dto.getAvailableAmount());
            deposit.setAccountAmount(dto.getAvailableAmount());
            // ... map other fields from the DTO to the entity

            // Handle nested SignerInfo list
            List<SignerInfo> newSignerInfos = dto.getSignerInfo().stream()
                    .map(signerDto -> SignerInfo.builder()
                            .customerNumber(signerDto.getCustomerNumber())
                            .portion(signerDto.getPortion())
                            .customerRelationWithDepositPersian(signerDto.getCustomerRelationWithDepositPersian())
                            .customerRelationWithDepositEnglish(signerDto.getCustomerRelationWithDepositEnglish())
                            .fullName(signerDto.getFullName())
                            .build())
                    .toList();

            // Clear existing list and add new ones (due to orphanRemoval = true)
            deposit.getSignerInfo().clear();
            deposit.getSignerInfo().addAll(newSignerInfos);

            // Save the updated entity
            depositRepository.save(deposit);
            System.out.println("Read model for deposit updated successfully.");
        });
    }
}