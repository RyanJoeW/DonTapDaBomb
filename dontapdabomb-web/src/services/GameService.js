import axios from 'axios';

const API_URL = 'http://localhost:8080/games';

export const startGame = (playerName, boardSize, numMines) => {
    return axios.post(`${API_URL}/start`, {
        playerName,
        boardSize,
        numMines,
    });
};

export const openCell = (gameId, cellIndex) => {
    return axios.post(`${API_URL}/open`, {
        gameId,
        cellIndex,
    });
};