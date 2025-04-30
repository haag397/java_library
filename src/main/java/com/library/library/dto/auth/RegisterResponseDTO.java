package com.library.library.dto.auth;

import lombok.Builder;

import java.util.UUID;

@Builder
public record RegisterResponseDTO(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String mobile) {}
