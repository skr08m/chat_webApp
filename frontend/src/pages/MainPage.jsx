import React, { useEffect, useState, useRef } from "react";
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
    addMessageListener,
} from "../services/stomp-socket";

export default function MainPage({ user }) {
    const navigate = useNavigate();

    const [rooms, setRooms] = useState([]);
    const [selectedRoomId, setSelectedRoomId] = useState(null);
    const [messagesByRoom, setMessagesByRoom] = useState({});

    const [newRoomName, setNewRoomName] = useState("");
    const [newRoomDescription, setNewRoomDescription] = useState("");

    // WebSocket接続済みかどうか
    const isSocketConnected = useRef(false);

    useEffect(() => {
        const init = async () => {
            try {
                const { rooms, messagesByRoom } = await fetchRoomsWithMessages();
                setRooms(rooms);
                setMessagesByRoom(messagesByRoom);
                if (rooms.length > 0) {
                    setSelectedRoomId(rooms[0].id);
                    if (!isSocketConnected.current) {
                        connectAllRoomsWebSocket(rooms);
                        isSocketConnected.current = true;
                    }
                }
            } catch (err) {
                alert("ルームの取得に失敗しました");
            }
        };
        init();
    }, []);

    useEffect(() => {
        const handleMessage = (roomId, newMsg) => {
            setMessagesByRoom((prev) => {
                const existing = prev[roomId] || [];

                // 重複チェック：もしすでに同じIDのメッセージがあれば追加しない
                if (existing.some((msg) => msg.id === newMsg.id)) {
                    return prev;
                }

                return {
                    ...prev,
                    [roomId]: [...existing, newMsg],
                };
            });
        };

        addMessageListener(handleMessage);
    }, []);

    const handleLogout = () => {
        localStorage.removeItem("token");
        closeAllSockets();
        isSocketConnected.current = false;
        navigate("/login");
    };

    const handleAddRoom = async (name, description) => {
        if (!name.trim()) {
            alert("ルーム名を入力してください");
            return;
        }
        try {
            const newRoom = await createRoom(name, description);
            setRooms((prev) => [...prev, newRoom]);
            setMessagesByRoom((prev) => ({ ...prev, [newRoom.id]: [] }));
            setSelectedRoomId(newRoom.id);

            // すでに接続済みなら追加のルームだけ接続
            if (isSocketConnected.current) {
                connectAllRoomsWebSocket([newRoom]);
            } else {
                connectAllRoomsWebSocket([newRoom]);
                isSocketConnected.current = true;
            }

            setNewRoomName("");
            setNewRoomDescription("");
        } catch (err) {
            alert("ルーム作成に失敗しました");
        }
    };

    const handleSendMessage = (content) => {
        if (!content.trim() || !selectedRoomId || !user) return;

        sendMessageToRoom(selectedRoomId, content);

        // 送信時に即時UIへ反映はしない（サーバーからの反射で表示する）
    };

    const selectedMessages = messagesByRoom[selectedRoomId] || [];
    const selectedRoom = rooms.find((r) => r.id === selectedRoomId);

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
                    currentUserId={user?.userId}
                    currentUsername={user?.username}
                />
            </div>
        </div>
    );
}