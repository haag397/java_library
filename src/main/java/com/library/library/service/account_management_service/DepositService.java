package com.library.library.service.account_management_service;

import com.library.library.dto.account_management_dto.UpdateDepositRequestDTO;
import com.library.library.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepositService {

    private final CoreDepositClient coreDepositClient;
    private final DepositRepository depositRepository;
    private final DepositMapper depositMapper;

    @Cacheable(value = "deposits", key = "#depositId", unless = "#forceUpdate")
    public UpdateDepositRequestDTO getDeposit(UUID depositId, boolean forceUpdate) {
        if (forceUpdate) {
            return fetchAndUpdate(depositId); // bypass cache manually
        }
        return fetchAndUpdate(depositId); // normal fetch if cache miss
    }

    @CachePut(value = "deposits", key = "#depositId")
    public UpdateDepositRequestDTO fetchAndUpdate(UUID depositId) {
        // 1️⃣ Fetch from core
        UpdateDepositRequestDTO coreResponse = coreDepositClient.getDeposit(depositId);

        // 2️⃣ Compare with local DB and update if changed
        DepositEntity existing = depositRepository.findById(depositId).orElse(null);
        DepositEntity newEntity = depositMapper.toEntity(coreResponse);

        if (existing == null || !existing.equals(newEntity)) {
            depositRepository.save(newEntity);
        }

        // 3️⃣ Return fresh data (cache updated via @CachePut)
        return coreResponse;
    }
}
