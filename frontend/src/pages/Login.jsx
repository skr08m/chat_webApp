import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

// ページ全体の背景とセンター揃え
const pageStyle = {
    minHeight: "100vh",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#e0f7fa", // パステルブルー
};

// フォーム全体
const formContainer = {
    width: "90%",
    maxWidth: "420px",
    backgroundColor: "#fff3e0", // コーラル系
    padding: "35px",
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
    backgroundColor: "#fff",
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

export default function Login({ setUser }) {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        alert("※次回以降はメール認証が必要です。");
        setUser({ email });
        navigate("/");
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

                    <button type="submit" style={buttonStyle}>ログイン</button>
                </form>
                <p style={{ marginTop: "18px" }}>
                    新規登録は <Link to="/register" style={linkStyle}>こちら</Link>
                </p>
            </div>
        </div>
    );
}
