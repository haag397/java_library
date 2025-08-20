package com.library.library.event.login;

public record OtpSentEvent(
        String sessionId,
        String channel) {}