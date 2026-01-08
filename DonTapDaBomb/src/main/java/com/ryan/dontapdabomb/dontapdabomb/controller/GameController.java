package com.ryan.dontapdabomb.dontapdabomb.controller;

import com.ryan.dontapdabomb.dontapdabomb.entity.Game;
import com.ryan.dontapdabomb.dontapdabomb.service.IGameService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/games")
public class GameController {

    private final IGameService gameService;

    public GameController(IGameService gameService) {
        this.gameService = gameService;
    }
         //test voor merge
    @PostMapping("/start")
    public Game startGame(@RequestBody StartGameRequest request) {
        return gameService.startGame(request.getUsername(), request.getPassword(), request.getBoardSize(), request.getNumMines(), request.getBetAmount());
    }

    @PostMapping("/open")
    public Game openCell(@RequestBody OpenCellRequest request) {
        return gameService.openCell(request.getGameId(), request.getCellIndex());
    }

    @PostMapping("/cashout")
    public Game cashOut(@RequestBody CashOutRequest request) {
        return gameService.cashOut(request.getGameId());
    }

    public static class StartGameRequest {
        private String username;
        private String password;
        private int boardSize;
        private int numMines;
        private double betAmount;

        // getters/setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public int getBoardSize() { return boardSize; }
        public void setBoardSize(int boardSize) { this.boardSize = boardSize; }
        public int getNumMines() { return numMines; }
        public void setNumMines(int numMines) { this.numMines = numMines; }
        public double getBetAmount() { return betAmount; }
        public void setBetAmount(double betAmount) { this.betAmount = betAmount; }
    }

    public static class OpenCellRequest {
        private Long gameId;
        private int cellIndex;
        // getters/setters
        public Long getGameId() { return gameId; }
        public void setGameId(Long gameId) { this.gameId = gameId; }
        public int getCellIndex() { return cellIndex; }
        public void setCellIndex(int cellIndex) { this.cellIndex = cellIndex; }
    }

    public static class CashOutRequest {
        private Long gameId;
        // getters/setters
        public Long getGameId() { return gameId; }
        public void setGameId(Long gameId) { this.gameId = gameId; }
    }
}
