package com.library.library.event.login;

public record LoginAttemptOpenedEvent(
        String sessionId,
        String userId,
        String user) {}