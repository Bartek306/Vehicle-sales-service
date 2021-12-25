package com.example.demo.repository;

import com.example.demo.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer>, JpaSpecificationExecutor<Announcement> {
    List<Announcement> findAllByOwnerLogin(String owner);

    List<Announcement> findAllByType(String type);
}
