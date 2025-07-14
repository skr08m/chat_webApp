import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

// 背景全体
const pageStyle = {
    minHeight: "100vh",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#e0f7fa", // パステルブルー
};

// フォームカード
const formStyle = {
    width: "90%",
    maxWidth: "420px",
    padding: "35px",
    backgroundColor: "#c8e6c9", // ミントグリーン
    borderRadius: "16px",
    boxShadow: "0 6px 20px rgba(0,0,0,0.15)",
    fontFamily: "'Segoe UI', sans-serif",
};

// 入力欄
const inputStyle = {
    width: "100%",
    padding: "12px 14px",
    marginBottom: "18px",
    borderRadius: "6px",
    border: "1px solid #ccc",
    fontSize: "16px",
    backgroundColor: "#ffffff",
};

// ボタン
const buttonStyle = {
    width: "100%",
    padding: "12px",
    backgroundColor: "#0077c2", // ディープブルー
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

export default function Register({ setUser }) {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        alert("※押下時、次回以降はメール認証が必要です。");
        setUser({ username, email });
        navigate("/");
    };

    return (
        <div style={pageStyle}>
            <div style={formStyle}>
                <h2 style={{ marginBottom: "25px", color: "#37474f" }}>新規登録</h2>
                <form onSubmit={handleSubmit}>
                    <label>ユーザー名</label>
                    <input
                        required
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        style={inputStyle}
                    />

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
                        登録
                    </button>
                </form>
                <p style={{ marginTop: "18px", fontSize: "14px" }}>
                    ログインは <Link to="/login" style={linkStyle}>こちら</Link>
                </p>
            </div>
        </div>
    );
}
