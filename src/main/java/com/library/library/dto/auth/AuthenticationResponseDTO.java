package com.library.library.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.UUID;

@Builder
public record AuthenticationResponseDTO (
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("refresh_token")
        UUID refreshToken){}
