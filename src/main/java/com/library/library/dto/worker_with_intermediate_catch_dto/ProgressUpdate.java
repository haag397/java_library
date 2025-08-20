package com.library.library.dto.worker_with_intermediate_catch_dto;

import java.time.Instant;
import java.util.Map;

public record ProgressUpdate(
        String applicationId,
        String taskType,      // e.g. "account validation"
        String phase,         // "STARTED" | "COMPLETED" | "FAILED"
        String message,       // detail or error
        Map<String, Object> data, // variables to expose
        Instant at
) {}