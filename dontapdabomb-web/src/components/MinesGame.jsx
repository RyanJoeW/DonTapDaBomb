import { useState } from 'react'
import axios from 'axios'
import './MinesGame.css'


function MinesGame() {
  const [playerName, setPlayerName] = useState('')
  const [betAmount, setBetAmount] = useState(1);
  const [boardSize, setBoardSize] = useState(25)
  const [numMines, setNumMines] = useState(5)
  const [game, setGame] = useState(null)
  const [revealed, setRevealed] = useState([]) // houdt bij welke cellen aangeklikt zijn

  // 🔹 Start nieuw spel
    const startGame = async () => {
        console.log("START CLICKED");
    if (!playerName) {
      alert('Enter your name!')
      return
    }

    try {
      const res = await axios.post('http://localhost:8080/games/start', {
        playerName,
        boardSize,
        numMines,
        betAmount,

      })
      setGame(res.data)
      setRevealed([]) // reset revealed cells
      console.log('✅ Game started:', res.data)
    } catch (err) {
      console.error('❌ Error starting game:', err)
    }
  }

  // 🔹 Klik op een cel
  const handleCellClick = async (index) => {
  if (!game || !game.active) return;
  if (revealed.includes(index)) return;

  try {
    const res = await axios.post('http://localhost:8080/games/open', {
      gameId: game.id,
      cellIndex: index,
    });

    setGame(res.data);
      console.log("revealedCells:", res.data.revealedCells);
      setRevealed((prev) => [...prev, index]);
  } catch (err) {
    console.error('Error opening cell:', err);
  }
};

  // 🔹 Restart knop
  const restartGame = () => {
    setGame(null)
    setRevealed([])
    setPlayerName('')
  }

  return (
    <div className="game-container">
      <h1>💣 Mines Game</h1>

      {!game ? (
        // Start panel
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

            <label>Bet Amount</label>
            <input
                type="number"
                value={betAmount}
                onChange={(e) => setBetAmount(Number(e.target.value))}
            />

          <button onClick={startGame}>Start Game</button>
        </div>
      ) : (
        // Game board
        <div className="game-board">
            <h3>Player: {game.playerName}</h3>
            <p>Bet: {game.betAmount}</p>
            <p>Profit: {game.profit.toFixed(2)}</p>
            <p>Multiplier: {game.multiplier.toFixed(2)}</p>
            <p>Status: {game.active ? '🟢 Active' : '🔴 Game Over'}</p>

          <div className="grid">
            {Array.from({ length: game.boardSize }).map((_, i) => (
              <button
                key={i}
                className={`cell ${revealed.includes(i) ? 'revealed' : ''}`}
                onClick={() => handleCellClick(i)}
              >
                {revealed.includes(i)
                  ? game.mines[i] === true
                    ? '💣'
                    : '💎'
                  : '?'}
              </button>
            ))}
          </div>

          {!game.active && (
            <button onClick={restartGame} className="restart-btn">
              🔁 Restart
            </button>
          )}
        </div>
      )}
    </div>
  )
}

export default MinesGame