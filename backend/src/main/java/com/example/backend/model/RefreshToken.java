package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Objects;


@Entity
@Table(name = "refresh_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "token")
    private String token;

    @Column(name = "expiry_date")
    private Instant expiryDate;
}
