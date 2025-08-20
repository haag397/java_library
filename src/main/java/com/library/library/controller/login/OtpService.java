package com.library.library.controller.login;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class OtpService {
    private final ConcurrentHashMap<Long, ActivatedJob> pendingValidationJobs = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Map<String, String>> taskStatuses = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Long> jobTimestamps = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, ActivatedJob> getPendingValidationJobs() {
        return pendingValidationJobs;
    }

    public Map<String, String> getTaskStatuses(Long processInstanceKey) {
        return taskStatuses.getOrDefault(processInstanceKey, new HashMap<>());
    }

    public void updateTaskStatus(Long processInstanceKey, String task, String status) {
        log.info("Updating status for process {}: {} = {}", processInstanceKey, task, status);
        taskStatuses.compute(processInstanceKey, (key, existing) -> {
            Map<String, String> statuses = (existing != null) ? new HashMap<>(existing) : new HashMap<>();
            statuses.put(task, status);
            return statuses;
        });
    }

    public void addPendingJob(Long processInstanceKey, ActivatedJob job) {
        log.info("Adding pending job for process {}", processInstanceKey);
        pendingValidationJobs.put(processInstanceKey, job);
        jobTimestamps.put(processInstanceKey, System.currentTimeMillis());
    }

    @Scheduled(fixedRate = 60000) // Run every minute
    public void cleanupStaleJobs() {
        long currentTime = System.currentTimeMillis();
        pendingValidationJobs.entrySet().removeIf(entry -> {
            Long timestamp = jobTimestamps.get(entry.getKey());
            if (timestamp != null && (currentTime - timestamp) > 150000) { // 2.5 minutes to be safe
                log.info("Removing stale job and statuses for process {}", entry.getKey());
                taskStatuses.remove(entry.getKey());
                jobTimestamps.remove(entry.getKey());
                return true;
            }
            return false;
        });
    }
}