package com.library.library.dto.check_info;

public record StartResponse(
        long processInstanceKey,
        String correlationKey) {}