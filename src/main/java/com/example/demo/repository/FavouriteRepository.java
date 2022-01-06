package com.example.demo.repository;

import com.example.demo.model.Favourite;
import com.example.demo.model.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavouriteRepository extends JpaRepository<Favourite, Integer> {
    Optional<Favourite> findByOwnerLogin(String login);
}
