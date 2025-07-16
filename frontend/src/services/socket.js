let sockets = {};
let listeners = [];

export function connectAllRoomsWebSocket(rooms) {
    rooms.forEach(room => {
        if (sockets[room.id]) return;

        const socket = new WebSocket(`ws://localhost:8080/ws/rooms/${room.id}`);

        socket.onopen = () => {
            console.log(`WebSocket connected to room ${room.id}`);
        };

        socket.onmessage = (event) => {
            const message = JSON.parse(event.data);
            listeners.forEach(listener => listener(room.id, message));
        };

        socket.onclose = () => {
            console.log(`WebSocket disconnected from room ${room.id}`);
            delete sockets[room.id];
        };

        socket.onerror = (error) => {
            console.error(`WebSocket error on room ${room.id}:`, error);
        };

        sockets[room.id] = socket;
    });
}

export function sendMessageToRoom(roomId, content) {
    const socket = sockets[roomId];
    if (socket && socket.readyState === WebSocket.OPEN) {
        const message = {
            content,
            timestamp: new Date().toISOString(),
            senderId: 1, // 仮の値、本来はログインユーザーID
        };
        socket.send(JSON.stringify(message));
    }
}

export function addMessageListener(callback) {
    listeners.push(callback);
}

export function closeAllSockets() {
    Object.values(sockets).forEach(socket => socket.close());
    sockets = {};
    listeners = [];
}
