package com.library.library.dto.user;

import com.library.library.model.Role;
import jakarta.annotation.Nullable;
import lombok.Builder;

@Builder
public record UserRequestDTO(
        @Nullable String email,
        @Nullable String firstName,
        @Nullable String lastName,
        @Nullable String mobile,
        @Nullable String password,
        @Nullable Role role) {}
