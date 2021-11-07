package com.example.demo.repository;

import com.example.demo.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer> {
    Optional<UserModel> findByLoginAndPassword(String login, String password);
    Optional<UserModel> findByEmail(String email);
    @Transactional
    @Modifying
    @Query("UPDATE UserModel user " +
            "SET user.active = TRUE WHERE user.email = ?1")
    int activeUserModel(String email);
}
