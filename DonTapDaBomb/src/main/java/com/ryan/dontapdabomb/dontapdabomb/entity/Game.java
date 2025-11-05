package com.ryan.dontapdabomb.dontapdabomb.entity;


import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String playerName;
    private int boardSize;     // totaal aantal vakjes
    private int numMines;      // aantal mijnen
    private boolean isActive;  // of het spel nog bezig is
    private double profit;     // winst tot nu toe

    @ElementCollection
    private List<Integer> openedCells; // welke cellen al open zijn

    @ElementCollection
    private List<Boolean> mines; // true = mijn aanwezig

    public Game() {}

    // constructor voor nieuwe games
    public Game(String playerName, int boardSize, int numMines) {
        this.playerName = playerName;
        this.boardSize = boardSize;
        this.numMines = numMines;
        this.isActive = true;
        this.profit = 0.0;
    }

    // getters en setters
    public Long getId() { return id; }
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public int getBoardSize() { return boardSize; }
    public void setBoardSize(int boardSize) { this.boardSize = boardSize; }
    public int getNumMines() { return numMines; }
    public void setNumMines(int numMines) { this.numMines = numMines; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public double getProfit() { return profit; }
    public void setProfit(double profit) { this.profit = profit; }
    public List<Integer> getOpenedCells() { return openedCells; }
    public void setOpenedCells(List<Integer> openedCells) { this.openedCells = openedCells; }
    public List<Boolean> getMines() { return mines; }
    public void setMines(List<Boolean> mines) { this.mines = mines; }
}
