package com.ryan.dontapdabomb.dontapdabomb.entity;


import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue
    private Long id;

    private int boardSize;
    private int numMines;
    private double betAmount;
    private double profit;
    private double multiplier;
    private boolean active;

    @ManyToOne
    private User user;

    @ElementCollection
    private List<Integer> openedCells;

    @ElementCollection
    private List<Boolean> mines;


    public Game() {}

    // constructor voor nieuwe games
    public Game(int boardSize, int numMines) {
        this.boardSize = boardSize;
        this.numMines = numMines;
        this.active = true;
        this.profit = 0.0;
    }

    // getters en setters
    public Long getId() { return id; }
    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}
    public int getBoardSize() { return boardSize; }
    public void setBoardSize(int boardSize) { this.boardSize = boardSize; }
    public int getNumMines() { return numMines; }
    public void setNumMines(int numMines) { this.numMines = numMines; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public double getProfit() { return profit; }
    public void setProfit(double profit) { this.profit = profit; }
    public List<Integer> getOpenedCells() { return openedCells; }
    public void setOpenedCells(List<Integer> openedCells) { this.openedCells = openedCells; }
    public List<Boolean> getMines() { return mines; }
    public void setMines(List<Boolean> mines) { this.mines = mines; }
    public double getBetAmount() { return betAmount; }
    public void setBetAmount(double betAmount) { this.betAmount = betAmount; }

    public double getMultiplier() { return multiplier; }
    public void setMultiplier(double multiplier) { this.multiplier = multiplier; }
}
