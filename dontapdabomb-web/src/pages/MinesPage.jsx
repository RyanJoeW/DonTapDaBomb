import { useNavigate } from 'react-router-dom'

function MinesPage() {
  const navigate = useNavigate()

  const handleStartClick = () => {
    navigate('/mines/game') // ✅ stuurt door naar de nieuwe route
  }

  return (
    <div>
      <h2>Mines Game</h2>
      <p>Hier komt straks het spel 👇</p>
      <button onClick={handleStartClick}>Start Game</button>
    </div>
  )
}

export default MinesPage