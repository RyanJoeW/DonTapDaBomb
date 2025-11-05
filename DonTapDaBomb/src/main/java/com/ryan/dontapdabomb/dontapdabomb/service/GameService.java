package com.ryan.dontapdabomb.dontapdabomb.service;

import com.ryan.dontapdabomb.dontapdabomb.entity.Game;

public interface GameService {
    Game startGame(String playerName, int boardSize, int numMines);
    Game getGameById(Long gameId);
    Game openCell(Long gameId, int cellIndex);
    Game cashOut(Long gameId);
}