package com.library.library.event.login;

public record OtpCheckedEvent(
        String sessionId,
        boolean valid,
        int attempts,
        String reason) {}
