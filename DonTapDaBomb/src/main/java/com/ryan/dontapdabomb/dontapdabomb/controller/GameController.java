package com.ryan.dontapdabomb.dontapdabomb.controller;

import com.ryan.dontapdabomb.dontapdabomb.entity.Game;
import com.ryan.dontapdabomb.dontapdabomb.service.GameService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start")
    public Game startGame(@RequestBody StartGameRequest request) {
        return gameService.startGame(request.getPlayerName(), request.getBoardSize(), request.getNumMines());
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
        private String playerName;
        private int boardSize;
        private int numMines;

        // getters/setters
        public String getPlayerName() { return playerName; }
        public void setPlayerName(String playerName) { this.playerName = playerName; }
        public int getBoardSize() { return boardSize; }
        public void setBoardSize(int boardSize) { this.boardSize = boardSize; }
        public int getNumMines() { return numMines; }
        public void setNumMines(int numMines) { this.numMines = numMines; }
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
