import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom'
import HomePage from './pages/HomePage'
import RegisterPage from './pages/RegisterPage'
import MinesPage from './pages/MinesPage'
import MinesGame from './components/MinesGame' // ✅ voeg dit toe
import Leaderboard from './components/Leaderboard.jsx'

function App() {
  return (
    <Router>
      <div className="main-container">
        <h1>dontapdabomb webapp</h1>

        {/* Navigatie menu */}
        <nav style={{ marginBottom: '20px' }}>
          <Link to="/home" style={{ marginRight: '10px' }}>🏠 Home</Link>
          <Link to="/register" style={{ marginRight: '10px' }}>🧍 Register</Link>
          <Link to="/mines">💣 Mines</Link>
          <Link to="/leaderboard" style={{ marginLeft: '10px' }}>🏆 Leaderboard</Link>
        </nav>

        {/* Routes (pagina's) */}
        <Routes>
          <Route path="/home" element={<HomePage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/mines" element={<MinesPage />} />
          <Route path="/mines/game" element={<MinesGame />} />
          <Route path="/leaderboard" element={<Leaderboard />} />
        </Routes>
      </div>
    </Router>
  )
}

export default App