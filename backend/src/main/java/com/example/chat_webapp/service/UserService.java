package com.example.chat_webapp.service;

import com.example.chat_webapp.dto.RegisterRequest;
import com.example.chat_webapp.entitiy.UsersModel;
import com.example.chat_webapp.repository.UserRepository;
import com.example.chat_webapp.security.JwtTokenProvider;

import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ユーザー登録処理
    @Transactional // トランザクション用アノテーション
    public UsersModel registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("このメールアドレスはすでに登録されています。");
        }

        UsersModel user = new UsersModel();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAuthenticated(false); // 初期状態は未認証（メール認証などを想定）

        return userRepository.save(user);
    }
}
