package com.example.chat_webapp.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.chat_webapp.entitiy.UsersModel;
import com.example.chat_webapp.repository.UserRepository;
import com.example.chat_webapp.security.JwtTokenProvider;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
            UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    // メールアドレスとパスワードで認証し、認証成功したらJWTトークンを生成して返す
    public String login(String email, String password) {
        // 認証トークンを作成
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
        // 認証実行
        Authentication authentication = authenticationManager.authenticate(authToken);
        // 認証が成功したら、SecurityContextHolderにAuthenticationを設定(後々の認証用に発行済みトークンとして保持しておく)
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 認証成功ならJWTを生成して返す
        return jwtTokenProvider.generateToken(email);
    }

    // 現在ログイン中のユーザー情報取得
    public UsersModel getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("ユーザーが認証されていません");
        }
        Object principal = authentication.getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        } else {
            throw new RuntimeException("ユーザー情報の取得に失敗しました");
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}