import { useEffect, useState } from 'react'
import axios from 'axios'
import "./Leaderboard.css";

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
        <div className="leaderboard-container">
            <div className="leaderboard-header">
                <h2>🏆 Leaderboard</h2>
                <div className="live-indicator">
                    <span className="live-dot" />
                    Live updates
                </div>
            </div>

            <div className="leaderboard-card">
                {entries.length === 0 ? (
                    <p className="leaderboard-empty">
                        No entries yet — cash out to appear here.
                    </p>
                ) : (
                    <div className="leaderboard-list">
                        {entries.map((entry, index) => {
                            const medal =
                                index === 0 ? "🥇" :
                                    index === 1 ? "🥈" :
                                        index === 2 ? "🥉" :
                                            `#${index + 1}`;

                            return (
                                <div
                                    key={index}
                                    className={`leaderboard-row ${index < 3 ? "top" : ""}`}
                                >
                                    <div className="leaderboard-left">
                                        <div className="leaderboard-rank">{medal}</div>
                                        <div>
                                            <div className="leaderboard-name">{entry.username}</div>
                                            <div className="leaderboard-date">
                                                {new Date(entry.createdAt).toLocaleString()}
                                            </div>
                                        </div>
                                    </div>

                                    <div className="leaderboard-score">
                                        € {Number(entry.score).toFixed(2)}
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                )}
            </div>

            <div className="leaderboard-footer">
                Shows top 10 highest cash-out scores
            </div>
        </div>
    );
}

export default Leaderboard