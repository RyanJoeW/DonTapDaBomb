import { useEffect, useState } from 'react'
import axios from 'axios'

function Leaderboard() {
    const [entries, setEntries] = useState([])

    useEffect(() => {

        axios.get('http://localhost:8080/leaderboard/top10')
            .then(res => {
                setEntries(res.data)
            })
            .catch(err => {
                console.error('Error fetching leaderboard:', err)
            })


        const eventSource = new EventSource('http://localhost:8080/leaderboard/stream')

        eventSource.onmessage = (event) => {
            const data = JSON.parse(event.data)
            setEntries(data)
        }

        eventSource.onerror = () => {
            console.error('SSE connection error')
            eventSource.close()
        }

        // cleanup
        return () => {
            eventSource.close()
        }
    }, [])

    return (
        <div>
            <h2>🏆 Leaderboard (Top 10)</h2>

            {entries.length === 0 ? (
                <p>No entries yet</p>
            ) : (
                <ul>
                    {entries.map((entry, index) => (
                        <li key={index}>
                            {index + 1}. {entry.username} – {entry.score.toFixed(2)}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    )
}

export default Leaderboard