import { useEffect, useRef, useState } from "react";
import axios from "axios";
import "./Leaderboard.css";

function Leaderboard() {
    const [entries, setEntries] = useState([]);
    const [fxMap, setFxMap] = useState({}); // { [key]: "is-new" | "moved-up" | "moved-down" }

    const prevIndexRef = useRef(new Map()); // key -> index
    const clearTimerRef = useRef(null);

    const getKey = (e) => {
        // Prefer: return String(e.id)
        return `${e.username}__${e.createdAt}`;
    };

    const applyFx = (nextEntries) => {
        const nextIndex = new Map();
        nextEntries.forEach((e, i) => nextIndex.set(getKey(e), i));

        const fx = {};
        for (const [key, nextI] of nextIndex.entries()) {
            const prevI = prevIndexRef.current.get(key);

            if (prevI === undefined) fx[key] = "is-new";
            else if (prevI > nextI) fx[key] = "moved-up";
            else if (prevI < nextI) fx[key] = "moved-down";
        }

        // Update prev
        prevIndexRef.current = nextIndex;

        // Trigger CSS anim classes
        setFxMap(fx);

        // Clear classes after animation window
        if (clearTimerRef.current) clearTimeout(clearTimerRef.current);
        clearTimerRef.current = setTimeout(() => setFxMap({}), 1400);
    };

    useEffect(() => {
        axios
            .get("http://localhost:8080/leaderboard/top10")
            .then((res) => {
                setEntries(res.data);
                applyFx(res.data);
            })
            .catch((err) => console.error("Error fetching leaderboard:", err));

        const eventSource = new EventSource("http://localhost:8080/leaderboard/stream");

        eventSource.onmessage = (event) => {
            const data = JSON.parse(event.data);
            setEntries(data);
            applyFx(data);
        };

        eventSource.onerror = () => {
            console.error("SSE connection error");
            eventSource.close();
        };

        return () => {
            eventSource.close();
            if (clearTimerRef.current) clearTimeout(clearTimerRef.current);
        };
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

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
                    <p className="leaderboard-empty">No entries yet — cash out to appear here.</p>
                ) : (
                    <div className="leaderboard-list">
                        {entries.map((entry, index) => {
                            const medal =
                                index === 0 ? "🥇" : index === 1 ? "🥈" : index === 2 ? "🥉" : `#${index + 1}`;

                            const key = getKey(entry);
                            const fxClass = fxMap[key] ? ` ${fxMap[key]}` : "";

                            return (
                                <div
                                    key={key}
                                    className={`leaderboard-row ${index < 3 ? "top" : ""}${fxClass}`}
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

                                    <div className="leaderboard-score">€ {Number(entry.score).toFixed(2)}</div>
                                </div>
                            );
                        })}
                    </div>
                )}
            </div>

            <div className="leaderboard-footer">Shows top 10 highest cash-out scores</div>
        </div>
    );
}

export default Leaderboard;