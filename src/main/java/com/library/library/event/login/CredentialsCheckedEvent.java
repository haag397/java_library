package com.library.library.event.login;

public record CredentialsCheckedEvent(
        String sessionId,
        boolean valid,
        String reason) {}