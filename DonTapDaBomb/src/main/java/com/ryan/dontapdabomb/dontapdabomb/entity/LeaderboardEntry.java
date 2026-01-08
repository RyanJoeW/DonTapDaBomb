package com.ryan.dontapdabomb.dontapdabomb.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "leaderboard_entry")
public class LeaderboardEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double score;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    public LeaderboardEntry() {}

    public LeaderboardEntry(User user, Game game, double score) {
        this.user = user;
        this.game = game;
        this.score = score;
        this.createdAt = LocalDateTime.now();
    }

    // getters & setters
    public Long getId() { return id; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game; }
}

