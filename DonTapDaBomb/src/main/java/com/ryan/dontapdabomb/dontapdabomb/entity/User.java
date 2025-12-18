package com.ryan.dontapdabomb.dontapdabomb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.persistence.*;
import jdk.jfr.DataAmount;


import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    @JsonIgnore
    private String password;
    private int cash;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Game> games;

    public User() {}
    public User (long id, String name, String password, int cash){
        this.id = id;
        this.name = name;
        this.password = password;
        this.cash = cash;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public int getCash() { return cash; }
    public void setCash(int cash) { this.cash = cash; }
    public List<Game> getGames() { return games; }
    public void setGames(List<Game> games) { this.games = games; }
}

