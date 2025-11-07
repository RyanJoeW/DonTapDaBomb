package com.ryan.dontapdabomb.dontapdabomb.service;

import com.ryan.dontapdabomb.dontapdabomb.entity.Game;
import com.ryan.dontapdabomb.dontapdabomb.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService implements IGameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Game startGame(String playerName, int boardSize, int numMines) {
        Game game = new Game(playerName, boardSize, numMines);

        List<Boolean> mines = new ArrayList<>();
        for (int i = 0; i < boardSize; i++) {
            mines.add(false);
        }

        for (int i = 0; i < numMines; i++) {
            int index;
            do {
                index = (int) (Math.random() * boardSize);
            } while (mines.get(index));
            mines.set(index, true);
        }

        game.setMines(mines);
        game.setOpenedCells(new ArrayList<>());
        game.setProfit(0.0);
        game.setActive(true);

        return gameRepository.save(game);
    }

    @Override
    public Game getGameById(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));
    }

    @Override
    public Game openCell(Long gameId, int cellIndex) {
        Game game = getGameById(gameId);
        if (!game.isActive()) {
            throw new RuntimeException("Game already finished");
        }

        List<Integer> opened = game.getOpenedCells();
        if (opened.contains(cellIndex)) {
            throw new RuntimeException("Cell already opened");
        }

        opened.add(cellIndex);

        // check mijn
        boolean mine = game.getMines().get(cellIndex);
        if (mine) {
            game.setActive(false); // game over
            game.setProfit(0.0);
        } else {
            // simpele multiplier logica: winst = aantal open vakjes
            game.setProfit(opened.size() * 1.0);
        }

        game.setOpenedCells(opened);
        return gameRepository.save(game);
    }

    @Override
    public Game cashOut(Long gameId) {
        Game game = getGameById(gameId);
        if (!game.isActive()) {
            throw new RuntimeException("Game already finished");
        }

        game.setActive(false);
        // bij cashout behouden we huidige profit
        return gameRepository.save(game);
    }
}