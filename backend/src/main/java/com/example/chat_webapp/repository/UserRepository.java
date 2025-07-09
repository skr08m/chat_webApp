package com.example.chat_webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chat_webapp.entitiy.UsersModel;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UsersModel, Long> {

    Optional<UsersModel> findByEmail(String email);

    boolean existsByEmail(String email);
}
