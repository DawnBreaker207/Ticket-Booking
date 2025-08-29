package com.example.backend.model;

import org.apache.ibatis.type.Alias;

import java.time.Instant;
import java.util.Objects;

@Alias("RefreshToken")
public class RefreshToken extends AbstractMappedEntity {
    private Long id;
    private User user;
    private String token;
    private Instant expiryDate;


    public RefreshToken() {
        super();
    }

    public RefreshToken(Long id, User user, String token, Instant expiryDate) {
        super();
        this.id = id;
        this.user = new User(user);
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return new User(user);
    }

    public void setUser(User user) {
        this.user = new User(user);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
                "id=" + id +
                ", user=" + user +
                ", token='" + token + '\'' +
                ", expiryDate=" + expiryDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RefreshToken that = (RefreshToken) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(token, that.token) && Objects.equals(expiryDate, that.expiryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, token, expiryDate);
    }
}
