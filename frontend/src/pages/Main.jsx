import React, { useState, useEffect, useRef } from "react";
import { styles } from "../styles";

const dummyRooms = [
    { id: 1, name: "ルーム1" },
    { id: 2, name: "ルーム2" },
    { id: 3, name: "ルーム3" },
];

const myUserId = 1;

const dummyMessagesByRoom = {
    1: [
        { id: 1, senderId: 1, content: "こんにちは", timestamp: "2025-07-14T10:00:00Z" },
        { id: 2, senderId: 2, content: "お疲れ様です", timestamp: "2025-07-14T10:01:00Z" },
    ],
    2: [
        { id: 3, senderId: 2, content: "ルーム2のメッセージ", timestamp: "2025-07-14T11:00:00Z" },
    ],
    3: [],
};

function formatTimestamp(isoString) {
    const d = new Date(isoString);
    return d.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}

export default function Main() {
    const [selectedRoomId, setSelectedRoomId] = useState(dummyRooms[0].id);
    const [messagesByRoom, setMessagesByRoom] = useState(dummyMessagesByRoom);
    const [newMessage, setNewMessage] = useState("");

    const messageAreaRef = useRef(null);

    const handleSend = () => {
        if (newMessage.trim() === "") return;

        const newMsg = {
            id: Date.now(),
            senderId: myUserId,
            content: newMessage,
            timestamp: new Date().toISOString(),
        };

        setMessagesByRoom(prev => ({
            ...prev,
            [selectedRoomId]: [...(prev[selectedRoomId] || []), newMsg],
        }));
        setNewMessage("");
    };

    const currentMessages = messagesByRoom[selectedRoomId] || [];

    // メッセージが変わるたびに自動スクロール
    useEffect(() => {
        if (messageAreaRef.current) {
            messageAreaRef.current.scrollTop = messageAreaRef.current.scrollHeight;
        }
    }, [currentMessages]);

    return (
        <div style={styles.container}>
            {/* ルーム一覧 */}
            <div style={{ ...styles.sidebar, display: "flex", flexDirection: "column", gap: "8px" }}>
                <h3 style={styles.sidebarTitle}>ルーム一覧</h3>
                {dummyRooms.map(room => (
                    <button
                        key={room.id}
                        style={{
                            ...styles.roomItem,
                            backgroundColor: room.id === selectedRoomId ? "#ffd54f" : styles.roomItem.backgroundColor,
                            fontWeight: room.id === selectedRoomId ? "bold" : "normal",
                        }}
                        onClick={() => setSelectedRoomId(room.id)}
                    >
                        {room.name}
                    </button>
                ))}
            </div>

            {/* チャット欄 */}
            <div style={styles.chatSection}>
                <div style={styles.chatHeader}>
                    <button style={styles.actionButton}>ルーム削除</button>
                    <button style={styles.actionButton}>退出</button>
                </div>

                <div
                    ref={messageAreaRef}
                    style={{
                        ...styles.messageArea,
                        display: "flex",
                        flexDirection: "column",
                        gap: "10px",
                    }}
                >
                    {currentMessages.map(({ id, senderId, content, timestamp }) => {
                        const isMine = senderId === myUserId;
                        return (
                            <div
                                key={id}
                                style={{
                                    ...styles.messageBubble,
                                    alignSelf: isMine ? "flex-end" : "flex-start",
                                    backgroundColor: isMine ? "#81c784" : "#c8e6c9",
                                    color: isMine ? "white" : "black",
                                    textAlign: isMine ? "right" : "left",
                                    maxWidth: "60%",
                                    marginLeft: isMine ? "auto" : 0,
                                    marginRight: isMine ? 0 : "auto",
                                    padding: "10px 15px",
                                    borderRadius: "15px",
                                    wordBreak: "break-word",
                                    boxShadow: "0 1px 3px rgba(0,0,0,0.2)",
                                    display: "inline-block",
                                }}
                            >
                                <div>{content}</div>
                                <div style={{ fontSize: "0.7em", marginTop: "4px", opacity: 0.7 }}>
                                    {formatTimestamp(timestamp)}
                                </div>
                            </div>
                        );
                    })}
                </div>

                <div style={styles.inputArea}>
                    <input
                        type="text"
                        value={newMessage}
                        onChange={(e) => setNewMessage(e.target.value)}
                        placeholder="メッセージを入力"
                        style={styles.input}
                        onKeyDown={(e) => { if (e.key === "Enter") handleSend(); }}
                    />
                    <button onClick={handleSend} style={styles.sendButton}>送信</button>
                </div>
            </div>
        </div>
    );
}
