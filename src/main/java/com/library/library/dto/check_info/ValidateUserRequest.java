package com.library.library.dto.check_info;

public record ValidateUserRequest(
        String username,
        String firstName,
        String lastName,
        Integer age,
        String correlationKey) {}