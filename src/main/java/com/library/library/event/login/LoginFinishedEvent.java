package com.library.library.event.login;

public record LoginFinishedEvent(
        String sessionId,
        boolean success,
        String reason) {}
