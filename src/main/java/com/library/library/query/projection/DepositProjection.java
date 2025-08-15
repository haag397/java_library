package com.library.library.query.projection;

import com.library.library.dto.account_management_dto.DepositMapper;
import com.library.library.dto.account_management_dto.DepositSaveRequestDTO;
import com.library.library.event.account_management_event.DepositUpdatedEvent;
import com.library.library.model.Deposit;
import com.library.library.model.SignerInfo;
import com.library.library.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DepositProjection {

    private final DepositRepository repo;
    private final DepositMapper mapper;
    private final CacheManager cacheManager; // to refresh cache after DB update

    @EventHandler
    @Transactional
    public void on(DepositUpdatedEvent evt) {
        DepositSaveRequestDTO dto = evt.getPayload();

        // upsert: reuse existing ID if present for same deposit number
        Deposit entity = repo.findByDepositNumber(dto.getDepositNumber())
                .map(existing -> {
                    // map incoming dto ONTO existing entity (manual or a second mapper)
                    Deposit fresh = mapper.toEntity(dto);
                    fresh.setId(existing.getId());
                    return fresh;
                })
                .orElseGet(() -> mapper.toEntity(dto));

        repo.save(entity);

        // refresh cache entry (key strategy — here by depositId)
        Cache cache = cacheManager.getCache("deposits");
        if (cache != null) cache.put(entity.getId().toString(), mapper.toResponseDTO(entity));
    }
}
