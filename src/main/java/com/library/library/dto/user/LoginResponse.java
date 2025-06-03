package com.library.library.dto.user;
import lombok.Builder;
import lombok.Data;

@Builder
public record LoginResponse(
         boolean is_successful,
         String request_uuid,
         String request_ip,
         int status_code,
         String message,
         Data data) {}
