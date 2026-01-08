package com.ryan.dontapdabomb.dontapdabomb.service;

import com.ryan.dontapdabomb.dontapdabomb.entity.Game;
import com.ryan.dontapdabomb.dontapdabomb.entity.User;
import com.ryan.dontapdabomb.dontapdabomb.entity.LeaderboardEntry;
import com.ryan.dontapdabomb.dontapdabomb.repository.GameRepository;
import com.ryan.dontapdabomb.dontapdabomb.repository.LeaderboardEntryRepository;
import org.springframework.stereotype.Service;
import com.ryan.dontapdabomb.dontapdabomb.exception .BadRequestException;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService implements IGameService {

    private final GameRepository gameRepository;
    private final UserService userService;
    private final LeaderboardEntryRepository leaderboardEntryRepository;
    private final LeaderboardService leaderboardService;

    public GameService(GameRepository gameRepository, UserService userService, LeaderboardEntryRepository leaderboardEntryRepository,  LeaderboardService leaderboardService) {
        this.gameRepository = gameRepository;
        this.userService = userService;
        this.leaderboardEntryRepository = leaderboardEntryRepository;
        this.leaderboardService = leaderboardService;
    }

    @Override
    public Game startGame(String username, String password, int boardSize, int numMines, double betAmount) {
        User user = userService.getAllUsers()
                .stream()
                .filter(u -> u.getName().equals(username))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new BadRequestException("Wrong password");
        }

        if (user.getCash() < betAmount) {
            throw new BadRequestException("Not enough cash");
        }

        user.setCash(user.getCash() - (int) betAmount);
        userService.updateUser(user);


        Game game = new Game(boardSize, numMines); // of lege constructor
        game.setUser(user);


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
        return gameRepository.findById(gameId).orElseThrow(() -> new BadRequestException("Game not found"));
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
        User user = game.getUser();
        user.setCash(user.getCash() + (int) game.getProfit());
        userService.updateUser(user);

        LeaderboardEntry entry = new LeaderboardEntry(game.getUser(), game, game.getProfit());
        leaderboardEntryRepository.save(entry);

        leaderboardService.addEntryAndNotify(entry);

        game.setActive(false);

        return gameRepository.save(game);
    }

}