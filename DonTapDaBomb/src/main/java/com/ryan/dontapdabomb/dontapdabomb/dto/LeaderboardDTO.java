package com.ryan.dontapdabomb.dontapdabomb.dto;

import java.time.LocalDateTime;

public class LeaderboardDTO {

    private String username;
    private double score;
    private LocalDateTime createdAt;

    public LeaderboardDTO(String username, double score, LocalDateTime createdAt) {
        this.username = username;
        this.score = score;
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public double getScore() {
        return score;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}