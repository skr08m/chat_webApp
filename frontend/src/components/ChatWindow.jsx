import React, { useState, useRef, useEffect } from "react";

export default function ChatWindow({ messages, selectedRoomId, setMessagesByRoom }) {
    const [newMessage, setNewMessage] = useState("");
    const messageAreaRef = useRef(null);

    const handleSend = () => {
        if (newMessage.trim() === "") return;

        const newMsg = {
            id: Date.now(),
            senderId: 1, // 仮のユーザーID
            content: newMessage,
            timestamp: new Date().toISOString(),
        };

        setMessagesByRoom(prev => ({
            ...prev,
            [selectedRoomId]: [...(prev[selectedRoomId] || []), newMsg],
        }));
        setNewMessage("");
    };

    useEffect(() => {
        if (messageAreaRef.current) {
            messageAreaRef.current.scrollTop = messageAreaRef.current.scrollHeight;
        }
    }, [messages]);

    return (
        <div style={{ flex: 1, display: "flex", flexDirection: "column", padding: "10px" }}>
            <div
                ref={messageAreaRef}
                style={{ flexGrow: 1, overflowY: "auto", marginBottom: "10px" }}
            >
                {messages.map(({ id, senderId, content, timestamp }) => (
                    <div key={id} style={{ marginBottom: "8px" }}>
                        <b>{senderId === 1 ? "自分" : `ユーザー${senderId}`}</b>: {content}
                        <div style={{ fontSize: "0.7em", color: "#666" }}>
                            {new Date(timestamp).toLocaleTimeString()}
                        </div>
                    </div>
                ))}
            </div>
            <input
                type="text"
                value={newMessage}
                onChange={e => setNewMessage(e.target.value)}
                onKeyDown={e => e.key === "Enter" && handleSend()}
                placeholder="メッセージを入力"
                style={{ padding: "8px", borderRadius: "4px", border: "1px solid #ccc" }}
            />
            <button onClick={handleSend} style={{ marginTop: "6px" }}>
                送信
            </button>
        </div>
    );
}
