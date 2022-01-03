package com.example.demo.repository;

import com.example.demo.model.Announcement;
import com.example.demo.model.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {
    Optional<History> findByOwnerLogin(String login);
}
