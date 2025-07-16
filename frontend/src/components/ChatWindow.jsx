import React, { useState, useRef, useEffect } from "react";

export default function ChatWindow({
    messages,
    selectedRoomId,
    onSendMessage,
    currentUserId,
    currentUsername // ← これを追加
}) {

    const [newMessage, setNewMessage] = useState("");
    const messageAreaRef = useRef(null);

    const handleSend = () => {
        if (newMessage.trim() === "") return;
        onSendMessage(newMessage);
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
                {messages.map(({ id, senderId, content, timestamp }) => {
                    console.log("senderId:", senderId, typeof senderId);//ログ
                    console.log("currentUserId:", currentUserId, typeof currentUserId);//ログ

                    const isOwnMessage = senderId === String(currentUserId);
                    return (
                        <div
                            key={id}
                            style={{
                                display: "flex",
                                justifyContent: isOwnMessage ? "flex-end" : "flex-start",
                                marginBottom: "8px"
                            }}
                        >
                            <div
                                style={{
                                    backgroundColor: isOwnMessage ? "#dcf8c6" : "#f1f0f0",
                                    padding: "8px 12px",
                                    borderRadius: "12px",
                                    maxWidth: "60%",
                                    wordWrap: "break-word"
                                }}
                            >
                                <b>{isOwnMessage ? "自分" : `ユーザー${senderId}`}</b>: {content}
                                <div style={{
                                    fontSize: "0.7em",
                                    color: "#666",
                                    textAlign: isOwnMessage ? "right" : "left"
                                }}>
                                    {new Date(timestamp).toLocaleTimeString()}
                                </div>
                            </div>
                        </div>
                    );
                })}
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
