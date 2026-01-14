import { useState } from 'react'
import axios from 'axios'
import './MinesGame.css'


function MinesGame() {
  const [username, setUsername] = useState('')
  const [betAmount, setBetAmount] = useState(1);
  const [password, setPassword] = useState('')
  const [boardSize, setBoardSize] = useState(25)
  const [numMines, setNumMines] = useState(5)
  const [game, setGame] = useState(null)
  const [revealed, setRevealed] = useState([]) // houdt bij welke cellen aangeklikt zijn
  const gridSize = game ? Math.sqrt(game.boardSize) : 0;

  // 🔹 Start nieuw spel
    const startGame = async () => {
        console.log("START CLICKED");
        if (!username || !password) {
            alert('Enter username and password!')
            return
        }

        const bet = Number(betAmount);
        const mines = Number(numMines);

        if (Number.isNaN(bet) || Number.isNaN(mines)) {
            alert("Fill in bet amount and number of mines");
            return;
        }

        if (bet <= 0 || mines <= 0) {
            alert("Values must be greater than 0");
            return;
        }



    try {
        const res = await axios.post('http://localhost:8080/games/start', {
            username,
            password,
            boardSize,
            numMines,
            betAmount,
        })
      setGame(res.data)
      setRevealed([]) // reset revealed cells
      console.log('✅ Game started:', res.data)
    } catch (err) {
        console.error('❌ Error starting game:', err)

        if (err.response && err.response.data) {
            if (typeof err.response.data === 'string') {
                alert(err.response.data)
            } else if (err.response.data.message) {
                alert(err.response.data.message)
            } else {
                alert('Error starting game')
            }
        } else {
            alert('Something went wrong while starting the game')
        }
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

    const handleCashOut = async () => {
        if (!game || !game.active) return;

        try {
            const res = await axios.post('http://localhost:8080/games/cashout', {
                gameId: game.id,
            });

            setGame(res.data);
            alert('Cashout successful!');
        } catch (err) {
            console.error('Error during cashout:', err);
            alert('Cashout failed');
        }
    };
  // 🔹 Restart knop
  const restartGame = () => {
    setGame(null)
    setRevealed([])
    setUsername('')
  }

  return (
    <div className="game-container">
      <h1>💣 Mines Game</h1>

      {!game ? (
        // Start panel
        <div className="start-panel">
            <label htmlFor="username">Username</label>
            <input
                id="username"
                type="text"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
            />

            <label htmlFor="password">Password</label>
            <input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />

            <label htmlFor="boardSize">Board Size</label>
            <select
                id="boardSize"
                value={boardSize}
                onChange={(e) => setBoardSize(Number(e.target.value))}
            >
                <option value={9}>3 x 3</option>
                <option value={16}>4 x 4</option>
                <option value={25}>5 x 5</option>
            </select>

            <label htmlFor="mines">Number of Mines</label>
            <input
                id="mines"
                type="number"
                value={numMines}
                onChange={(e) => setNumMines(e.target.value)}
            />

            <label htmlFor="bet">Bet Amount</label>
            <input
                id="bet"
                type="number"
                value={betAmount}
                onChange={(e) => setBetAmount(e.target.value)}
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
            {game.active && (
                <button onClick={handleCashOut} className="cashout-btn">
                    💰 Cash Out
                </button>
            )}

            <div
                className="grid"
                style={{ gridTemplateColumns: `repeat(${gridSize}, 60px)` }}
            >
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