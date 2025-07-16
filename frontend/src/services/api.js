import axios from "axios";

const API_BASE = import.meta.env.VITE_API_BASE_URL;

const apiClient = axios.create({
    baseURL: API_BASE,
});

apiClient.interceptors.request.use(config => {
    const token = localStorage.getItem("token");
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export async function fetchRoomsWithMessages() {
    const roomsRes = await apiClient.get("/rooms");
    const rooms = roomsRes.data;

    const messagesByRoom = {};
    for (const room of rooms) {
        const messagesRes = await apiClient.get(`/messages/${room.id}`);
        messagesByRoom[room.id] = messagesRes.data;
    }

    return { rooms, messagesByRoom };
}

export async function createRoom(name, description) {
    const res = await apiClient.post("/rooms", { name, description });
    return res.data;
}
