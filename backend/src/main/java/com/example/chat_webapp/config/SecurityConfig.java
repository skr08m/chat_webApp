package com.example.chat_webapp.config;

import com.example.chat_webapp.security.CustomUserDetailsService;
import com.example.chat_webapp.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    /*
     * 本番用
     * 
     * @Bean
     * public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
     * return http
     * .cors(Customizer.withDefaults()) // ← CORS有効化
     * .csrf(csrf -> csrf.disable())
     * .authorizeHttpRequests(auth -> auth
     * .requestMatchers("/api/auth/login", "/api/users/register", "/ws/**",
     * "/app/**").permitAll()
     * .anyRequest().authenticated())
     * .sessionManagement(sess ->
     * sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
     * .addFilterBefore(jwtAuthenticationFilter,
     * UsernamePasswordAuthenticationFilter.class)
     * .build();
     * }
     */
    //テスト用
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable()) // CSRF無効化（開発用）
                .headers(headers -> headers.frameOptions().disable()) // iframe許可（H2コンソール用）
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/users/register",
                                "/ws/**",
                                "/app/**",
                                "/h2-console/**" // ← ここを追加
                        ).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:5173"); // フロントのURL
        configuration.addAllowedMethod("*"); // GET, POST, etc.
        configuration.addAllowedHeader("*"); // Authorization などを許可
        configuration.setAllowCredentials(true); // 認証情報付きリクエストを許可

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
