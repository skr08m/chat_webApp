import React from "react";

export default function ChatHeader({ room }) {
    return (
        <div style={{ padding: "10px", borderBottom: "1px solid #ccc" }}>
            <h2>{room ? room.name : "ルーム未選択"}</h2>
        </div>
    );
}
