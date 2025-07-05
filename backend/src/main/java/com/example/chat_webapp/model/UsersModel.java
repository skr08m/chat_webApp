package com.example.chat_webapp.model;

import jakarta.persistence.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.UUID;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

@Entity
@Table(name = "users")
public class UsersModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(length = 255)
    private String salt;

    @Column(name = "authenticated", nullable = false)
    private boolean isAuthenticated = false;

    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;

    public UsersModel(String email, String password) {
        this.email = email;
        generateSalt();
        this.passwordHash = hashPassword(password, salt);
    }

    // --- ソルト自動生成メソッド ---
    public void generateSalt() {
        this.salt = UUID.randomUUID().toString();
    }

    // ハッシュ化関数
    private String hashPassword(String password, String saltBase64) {
        try {
            byte[] saltBytes = Base64.getDecoder().decode(saltBase64);
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error while hashing password", e);
        }
    }

    // パスワード検証用メソッド
    public boolean verifyPassword(String rawPassword) {
        String hashedInput = hashPassword(rawPassword, this.salt);
        return hashedInput.equals(this.passwordHash);
    }

    // --- getter/setter省略可（Lombok可） ---
}
