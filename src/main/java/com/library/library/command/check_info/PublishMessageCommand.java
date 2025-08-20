package com.library.library.command.check_info;

import java.util.Map;

public record PublishMessageCommand(
        String correlationKey,
        Map<String, Object> payload) {}