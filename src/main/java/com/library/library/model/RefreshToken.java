package com.library.library.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

import static jakarta.persistence.GenerationType.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = UUID)
    @Column(name = "id", updatable = false, nullable = false)
    public Integer id;

    @Column(name = "refresh_token", nullable = false, unique = true, updatable = false)
    public String refreshToken;

    @Column(name = "created_at",nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "expired_at", nullable = false)
    private Instant expiredAt;

    @ManyToOne(optional = false)
    private User user;

}