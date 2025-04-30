package com.library.library.dto.error;

import lombok.Builder;

@Builder
public record ErrorResponseDTO
    (int status,
    String error){}
