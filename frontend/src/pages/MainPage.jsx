// src/pages/MainPage.jsx
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { styles } from "../styles";
import ChatSidebar from "../components/ChatSidebar";
import ChatHeader from "../components/ChatHeader";
import ChatWindow from "../components/ChatWindow";
import { fetchRoomsWithMessages, createRoom } from "../services/api";
import {
    connectAllRoomsWebSocket,
    closeAllSockets,
    sendMessageToRoom,
    addMessageListener
} from "../services/stomp-socket";

export default function MainPage() {
    const navigate = useNavigate();

    const [rooms, setRooms] = useState([]);
    const [selectedRoomId, setSelectedRoomId] = useState(null);
    const [messagesByRoom, setMessagesByRoom] = useState({});

    const [newRoomName, setNewRoomName] = useState("");
    const [newRoomDescription, setNewRoomDescription] = useState("");

    useEffect(() => {
        const init = async () => {
            try {
                const { rooms, messagesByRoom } = await fetchRoomsWithMessages();
                setRooms(rooms);
                setMessagesByRoom(messagesByRoom);
                if (rooms.length > 0) {
                    setSelectedRoomId(rooms[0].id);
                    connectAllRoomsWebSocket(rooms);
                }
            } catch (err) {
                alert("ルームの取得に失敗しました");
            }
        };
        init();
    }, []);

    useEffect(() => {
        const handleMessage = (roomId, newMsg) => {
            setMessagesByRoom(prev => ({
                ...prev,
                [roomId]: [...(prev[roomId] || []), newMsg],
            }));
        };
        addMessageListener(handleMessage);
    }, []);

    const handleLogout = () => {
        localStorage.removeItem("token");
        closeAllSockets();
        navigate("/login");
    };

    const handleAddRoom = async (name, description) => {
        if (!name.trim()) {
            alert("ルーム名を入力してください");
            return;
        }
        try {
            const newRoom = await createRoom(name, description,);
            setRooms((prev) => [...prev, newRoom]);
            setMessagesByRoom((prev) => ({ ...prev, [newRoom.id]: [] }));
            setSelectedRoomId(newRoom.id);
            connectAllRoomsWebSocket([newRoom]);
            setNewRoomName("");
            setNewRoomDescription("");
        } catch (err) {
            alert("ルーム作成に失敗しました");
        }
    };

    const handleSendMessage = (content) => {
        if (!content.trim() || !selectedRoomId) return;
        sendMessageToRoom(selectedRoomId, content);
    };

    const selectedMessages = messagesByRoom[selectedRoomId] || [];
    const selectedRoom = rooms.find(r => r.id === selectedRoomId);

    return (
        <div style={styles.container}>
            <ChatSidebar
                rooms={rooms}
                selectedRoomId={selectedRoomId}
                onSelectRoom={setSelectedRoomId}
                onAddRoom={handleAddRoom}
                onLogout={handleLogout}
                newRoomName={newRoomName}
                setNewRoomName={setNewRoomName}
                newRoomDescription={newRoomDescription}
                setNewRoomDescription={setNewRoomDescription}
            />
            <div style={styles.chatSection}>
                <ChatHeader room={selectedRoom} />
                <ChatWindow
                    messages={selectedMessages}
                    selectedRoomId={selectedRoomId}
                    onSendMessage={handleSendMessage}
                />
            </div>
        </div>
    );
}