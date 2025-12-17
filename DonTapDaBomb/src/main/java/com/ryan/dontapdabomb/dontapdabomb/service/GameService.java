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
    public Game startGame(String playerName, int boardSize, int numMines, double betAmount) {
        System.out.println("➡️ startGame reached");
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
        game.setBetAmount(betAmount);
        game.setMultiplier(1.0);

        System.out.println("➡️ startGame returned successfully");
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
        game.setOpenedCells(opened);

        boolean mine = game.getMines().get(cellIndex);
        if (mine) {
            game.setActive(false);
            game.setProfit(0.0);
            return gameRepository.save(game);
        }

        int totalSafe = game.getBoardSize() - game.getNumMines();
        int openedSafe = opened.size();
        int remainingSafe = totalSafe - openedSafe;
        int remainingClosed = game.getBoardSize() - openedSafe;

        double chanceSafe = (double) remainingSafe / remainingClosed;
        double multiplier = 1.0 / chanceSafe;

        game.setMultiplier(multiplier);

        double profit = game.getBetAmount() * multiplier;
        game.setProfit(profit);

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