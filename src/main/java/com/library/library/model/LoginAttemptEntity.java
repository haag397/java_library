//package com.library.library.model;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
//import lombok.*;
//
//import java.time.Instant;
//import java.util.UUID;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class LoginAttemptEntity {
//    @Id
//    @Column(name = "id", nullable = false, updatable = false, unique = true)
//    private UUID id;
//
//    private String userId;
//    private String user;
//    private String status;       // STARTED / OTP_SENT / SUCCESS / FAILED
//    private String reason;
//    private int attempts;
//    private Instant expiresAt;
//    private Instant updatedAt;
//
//    // getters/setters/constructors
//    // ...
//}