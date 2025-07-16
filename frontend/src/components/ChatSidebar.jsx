// src/components/ChatSidebar.jsx
import React, { useState } from "react";

export default function ChatSidebar({
    rooms,
    selectedRoomId,
    onSelectRoom,
    onAddRoom,
    onLogout,
}) {
    const [newRoomName, setNewRoomName] = useState("");
    const [newRoomDescription, setNewRoomDescription] = useState("");

    const handleAdd = async () => {
        const trimmedName = newRoomName.trim();
        if (!trimmedName) {
            alert("ルーム名を入力してください");
            return;
        }
        try {
            const newRoom = await onAddRoom(trimmedName, newRoomDescription);
            setNewRoomName("");
            setNewRoomDescription("");
        } catch (err) {
            alert("ルームの作成に失敗しました");
        }
    };

    return (
        <div style={{ width: "280px", padding: "12px", borderRight: "1px solid #ccc", height: "100vh", display: "flex", flexDirection: "column" }}>
            <div style={{ marginBottom: "12px" }}>
                <input
                    type="text"
                    placeholder="ルーム名"
                    value={newRoomName}
                    onChange={(e) => setNewRoomName(e.target.value)}
                    style={{ width: "100%", marginBottom: "6px" }}
                />
                <input
                    type="text"
                    placeholder="説明（任意）"
                    value={newRoomDescription}
                    onChange={(e) => setNewRoomDescription(e.target.value)}
                    style={{ width: "100%", marginBottom: "6px" }}
                />
                <button onClick={handleAdd} style={{ width: "100%" }}>ルーム追加</button>
            </div>

            <h3>ルーム一覧</h3>
            <div style={{ flexGrow: 1, overflowY: "auto" }}>
                {rooms.map((room) => (
                    <button
                        key={room.id}
                        onClick={() => onSelectRoom(room.id)}
                        style={{
                            display: "block",
                            width: "100%",
                            padding: "8px",
                            backgroundColor: room.id === selectedRoomId ? "#ffd54f" : "#eee",
                            marginBottom: "4px",
                            borderRadius: "6px",
                            textAlign: "left",
                        }}
                    >
                        <div>{room.name}</div>
                        {room.description && <small>{room.description}</small>}
                    </button>
                ))}
            </div>

            <button onClick={onLogout} style={{ marginTop: "auto", backgroundColor: "#d32f2f", color: "white", padding: "10px", borderRadius: "6px" }}>
                ログアウト
            </button>
        </div>
    );
}
