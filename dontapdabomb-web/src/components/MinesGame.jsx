import { useState } from 'react';
import { startGame, openCell } from '../services/gameService';
import './MinesGame.css';

function MinesGame() {
    const [playerName, setPlayerName] = useState('');
    const [boardSize, setBoardSize] = useState(25);
    const [numMines, setNumMines] = useState(5);
    const [game, setGame] = useState(null);
    const [revealed, setRevealed] = useState([]);

    // Start nieuw spel
    const handleStartGame = async () => {
        if (!playerName) {
            alert('Enter your name!');
            return;
        }

        try {
            const res = await startGame(playerName, boardSize, numMines);
            console.log("Game started:", res.data);

            setGame(res.data);
            setRevealed([]);
        } catch (err) {
            console.error('Error starting game:', err);
        }
    };

    // Klik op cel
    const handleCellClick = async (index) => {
        if (!game || !game.active) return;
        if (revealed.includes(index)) return;

        try {
            const res = await openCell(game.id, index);

            console.log("revealedCells:", res.data.revealedCells);

            // update game
            setGame(res.data);

            // markeer deze cel als onthuld
            setRevealed((prev) => [...prev, index]);

        } catch (err) {
            console.error("Error opening cell:", err);
        }
    };

    // Restart
    const restartGame = () => {
        setGame(null);
        setRevealed([]);
        setPlayerName('');
    };

    return (
        <div className="game-container">
            <h1>💣 Mines Game</h1>

            {!game ? (
                <div className="start-panel">
                    <label>Player Name</label>
                    <input
                        type="text"
                        value={playerName}
                        onChange={(e) => setPlayerName(e.target.value)}
                    />

                    <label>Board Size</label>
                    <input
                        type="number"
                        value={boardSize}
                        onChange={(e) => setBoardSize(Number(e.target.value))}
                    />

                    <label>Number of Mines</label>
                    <input
                        type="number"
                        value={numMines}
                        onChange={(e) => setNumMines(Number(e.target.value))}
                    />

                    <button onClick={handleStartGame}>Start Game</button>
                </div>
            ) : (
                <div className="game-board">
                    <h3>Player: {game.playerName}</h3>
                    <p>Profit: {game.profit}</p>
                    <p>Status: {game.active ? '🟢 Active' : '🔴 Game Over'}</p>

                    <div className="grid">
                        {Array.from({ length: game.boardSize }).map((_, i) => {
                            const isRevealed = revealed.includes(i);
                            const cellValue = game.revealedCells?.[i]; // verwacht: "M" of "C"

                            return (
                                <button
                                    key={i}
                                    className={`cell ${isRevealed ? 'revealed' : ''}`}
                                    onClick={() => handleCellClick(i)}
                                >
                                    {isRevealed
                                        ? cellValue === 'M'
                                            ? '💣'
                                            : '💎'
                                        : '?'}
                                </button>
                            );
                        })}
                    </div>

                    {!game.active && (
                        <button onClick={restartGame} className="restart-btn">
                            🔁 Restart
                        </button>
                    )}
                </div>
            )}
        </div>
    );
}

export default MinesGame;