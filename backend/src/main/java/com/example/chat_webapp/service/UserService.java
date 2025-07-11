package com.example.chat_webapp.service;

import com.example.chat_webapp.dto.RegisterRequest;
import com.example.chat_webapp.entitiy.UsersModel;
import com.example.chat_webapp.repository.UserRepository;
import com.example.chat_webapp.security.JwtTokenProvider;

import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //ユーザー登録処理
    @Transactional
    public void registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("このメールアドレスはすでに登録されています。");
        }

        UsersModel user = new UsersModel();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAuthenticated(false); // 初期状態は未認証（メール認証などを想定）

        userRepository.save(user);
    }

    /**
     * ログイン処理 + JWT発行
     */
    public String login(String email, String password) {
        // 認証トークンの生成
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);

        // Spring Security による認証処理
        Authentication authentication = authenticationManager.authenticate(authToken);

        // ユーザーIDの取得（JWTのSubjectに使用）
        UsersModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return jwtTokenProvider.generateToken(user.getUserId().toString());
    }
}
