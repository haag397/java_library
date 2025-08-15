package com.library.library.service.account_management_service;

import com.library.library.command.accountmangement.UpdateDepositCommand;
import com.library.library.dto.account_management_dto.DepositMapper;
import com.library.library.dto.account_management_dto.DepositResponseDTO;
import com.library.library.dto.account_management_dto.DepositSaveRequestDTO;
import com.library.library.model.Deposit;
import com.library.library.repository.CoreDepositClient;
import com.library.library.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepositQueryService {

    private final CacheManager cacheManager;
    private final DepositRepository repo;
    private final CoreDepositClient coreClient;
    private final DepositMapper mapper;
    private final CommandGateway commandGateway;

    // -------- Scenario A & B --------
    public DepositResponseDTO getDeposit(UUID depositId, String userId, String depositNumber, boolean forceUpdate) {

        String cacheKey = depositId.toString();
        Cache cache = cacheManager.getCache("deposits");

        if (!forceUpdate && cache != null) {
            DepositResponseDTO cached = cache.get(cacheKey, DepositResponseDTO.class);
            if (cached != null) return cached; // Scenario A: cache hit
        }

        // Miss or force: call core
        DepositSaveRequestDTO coreDto = coreClient.fetchDeposit(userId, depositNumber);

        // Publish command → event → projection persists + refreshes cache
        UpdateDepositCommand cmd = new UpdateDepositCommand(depositId, userId, coreDto);
        commandGateway.sendAndWait(cmd);

        // Build response immediately (no need to re-read DB)
        Deposit entityShape = mapper.toEntity(coreDto);
        entityShape.setId(resolveExistingIdOr(depositId, coreDto)); // keep consistent ID
        DepositResponseDTO response = mapper.toResponseDTO(entityShape);

        // Put into cache now for immediate hit (TTL handled by Redis config)
        if (cache != null) cache.put(cacheKey, response);

        return response;
    }

    private UUID resolveExistingIdOr(UUID depositId, DepositSaveRequestDTO dto) {
        return repo.findByDepositNumber(dto.getDepositNumber())
                .map(Deposit::getId)
                .orElse(depositId);
    }
}

