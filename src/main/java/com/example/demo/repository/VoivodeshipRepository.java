package com.example.demo.repository;

import com.example.demo.model.Voivodeship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoivodeshipRepository extends JpaRepository<Voivodeship, Integer> {
    Optional<Voivodeship> findByName(String name);

}
