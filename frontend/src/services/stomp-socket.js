import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

let stompClient = null;
let subscriptions = {};
let listeners = [];

export function connectAllRoomsWebSocket(rooms) {
    if (stompClient && stompClient.connected) {
        // すでに接続済みなら購読だけ追加
        rooms.forEach((room) => subscribeToRoom(room.id));
        return;
    }

    const token = localStorage.getItem("token");

    stompClient = new Client({
        brokerURL: null,
        webSocketFactory: () => new SockJS(`http://localhost:8080/ws-chat?token=${token}`),
        connectHeaders: {
            Authorization: `Bearer ${token}`,
        },
        reconnectDelay: 5000,
        onConnect: () => {
            console.log("STOMP connected");
            rooms.forEach((room) => subscribeToRoom(room.id));
        },
        onStompError: (frame) => {
            console.error("Broker error:", frame.headers["message"]);
        },
        onWebSocketError: (error) => {
            console.error("WebSocket error:", error);
        },
        debug: (str) => {
            // console.log("[STOMP DEBUG] " + str);
        },
    });

    stompClient.activate();
}

export function sendMessageToRoom(roomId, content) {
    if (!stompClient || !stompClient.connected) return;

    const payload = {
        messageRequest: content,
    };

    stompClient.publish({
        destination: `/app/chat/send/${roomId}`,
        body: JSON.stringify(payload),
        // headers は不要。接続時に認証済み
    });
}

export function addMessageListener(callback) {
    listeners.push(callback);
}

export function closeAllSockets() {
    Object.values(subscriptions).forEach((sub) => sub.unsubscribe());
    subscriptions = {};
    if (stompClient) stompClient.deactivate();
    stompClient = null;
    listeners = [];
}

function subscribeToRoom(roomId) {
    if (!stompClient || !stompClient.connected) {
        console.error("STOMP client is not connected. Cannot subscribe.");
        return;
    }

    // すでにそのルームを購読済みの場合は何もしない
    if (subscriptions[roomId]) {
        console.log(`Already subscribed to room ${roomId}`);
        return;
    }

    // 特定のルームIDのトピックを購読
    const subscription = stompClient.subscribe(`/topic/room.${roomId}`, (message) => {
        try {
            const receivedMessage = JSON.parse(message.body);
            // 登録されている全てのリスナー関数（MainPageのhandleMessage）を実行
            listeners.forEach(callback => callback(roomId, receivedMessage));
        } catch (error) {
            console.error("Failed to parse message body:", message.body, error);
        }
    });

    // 購読情報を保存しておく（後で購読解除できるようにするため）
    subscriptions[roomId] = subscription;
    console.log(`Subscribed to /topic/room.${roomId}`);
}
