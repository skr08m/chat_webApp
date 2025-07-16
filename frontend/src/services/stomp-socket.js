import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

let stompClient = null;
let subscriptions = {};
let listeners = [];

export function connectAllRoomsWebSocket(rooms) {
    if (stompClient && stompClient.connected) return;

    stompClient = new Client({
        brokerURL: null, // SockJS を使う場合は null
        webSocketFactory: () => {
            const token = localStorage.getItem("token");
            return new SockJS(`http://localhost:8080/ws-chat?token=${token}`);
        },
        reconnectDelay: 5000,
        onConnect: () => {
            console.log("STOMP connected");

            rooms.forEach((room) => {
                const topic = `/topic/room.${room.id}`;
                const sub = stompClient.subscribe(topic, (message) => {
                    const msg = JSON.parse(message.body);
                    listeners.forEach((cb) => cb(room.id, msg));
                });
                subscriptions[room.id] = sub;
            });
        },
        onStompError: (frame) => {
            console.error("Broker error:", frame.headers["message"]);
        },
    });

    stompClient.activate();
}

export function sendMessageToRoom(roomId, content) {
    if (!stompClient || !stompClient.connected) return;

    const token = localStorage.getItem("token");
    const payload = {
        messageRequest: content,
    };

    stompClient.publish({
        destination: `/app/chat/send/${roomId}`,
        body: JSON.stringify(payload),
        //headers: {
        //    Authorization: `Bearer ${token}`, // 認証必要ならここで送る
        //},
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
