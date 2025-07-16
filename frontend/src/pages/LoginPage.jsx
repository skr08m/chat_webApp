import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";

const pageStyle = {
    minHeight: "100vh",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#e0f7fa",
};
const formContainer = {
    width: "90%",
    maxWidth: "420px",
    backgroundColor: "#fff3e0",
    padding: "35px",
    borderRadius: "16px",
    boxShadow: "0 6px 20px rgba(0,0,0,0.15)",
    fontFamily: "'Segoe UI', sans-serif",
};
const inputStyle = {
    width: "100%",
    padding: "12px 14px",
    marginBottom: "18px",
    borderRadius: "6px",
    border: "1px solid #ccc",
    fontSize: "16px",
    backgroundColor: "#fff",
};
const buttonStyle = {
    width: "100%",
    padding: "12px",
    backgroundColor: "#0077c2",
    color: "white",
    fontWeight: "bold",
    border: "none",
    borderRadius: "6px",
    fontSize: "16px",
    cursor: "pointer",
    transition: "background-color 0.3s ease",
};
const linkStyle = {
    color: "#0077c2",
    textDecoration: "none",
};

const API_BASE = import.meta.env.VITE_API_BASE_URL;

export default function LoginPage({ setUser }) {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            // 1. ログインAPIへPOST
            const loginRes = await axios.post(`${API_BASE}/auth/login`, { email, password }, { withCredentials: true });

            // 2. トークンをlocalStorageに保存
            const token = loginRes.data.token;
            localStorage.setItem("token", token);
            console.log("受け取ったトークン: " + token);

            // 3. ユーザー情報取得APIを呼び出す（トークンをAuthorizationヘッダーにセット）
            const meRes = await axios.get(`${API_BASE}/auth/me`, {
                headers: { Authorization: `Bearer ${token}` },
            });

            // 4. 親コンポーネントにユーザー情報をセット
            setUser(meRes.data);

            // 5. メインページへ遷移
            navigate("/");
        } catch (error) {
            if (error.response) {
                alert("ログイン失敗: " + (error.response.data.message || error.response.statusText));
            } else {
                alert("通信エラーが発生しました。");
            }
            console.error(error);
        }
    };

    return (
        <div style={pageStyle}>
            <div style={formContainer}>
                <h2 style={{ color: "#37474f", marginBottom: "25px" }}>ログイン</h2>
                <form onSubmit={handleSubmit}>
                    <label>メールアドレス</label>
                    <input
                        type="email"
                        required
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        style={inputStyle}
                    />

                    <label>パスワード</label>
                    <input
                        type="password"
                        required
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        style={inputStyle}
                    />

                    <button type="submit" style={buttonStyle}>
                        ログイン
                    </button>
                </form>
                <p style={{ marginTop: "18px" }}>
                    新規登録は <Link to="/register" style={linkStyle}>こちら</Link>
                </p>
            </div>
        </div>
    );
}
