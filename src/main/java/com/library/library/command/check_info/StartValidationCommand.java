package com.library.library.command.check_info;

public record StartValidationCommand(
        String username,
        String firstName,
        String lastName,
        Integer age,
        String correlationKey) {}