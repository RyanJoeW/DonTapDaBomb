package com.ryan.dontapdabomb.dontapdabomb.service;

import com.ryan.dontapdabomb.dontapdabomb.entity.Game;

public interface IGameService {
    Game startGame(String username, String password, int boardSize, int numMines, double betAmount);
    Game getGameById(Long gameId);
    Game openCell(Long gameId, int cellIndex);
    Game cashOut(Long gameId);
}