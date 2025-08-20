package com.library.library.dto.worker_with_intermediate_catch_dto;

import java.util.Map;

public record MessageRequest(String correlationKey, Map<String, Object> payload) {}
