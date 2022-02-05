package com.example.demo.repository;

import com.example.demo.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer>, JpaSpecificationExecutor<Announcement> {
    List<Announcement> findAllByOwnerLogin(String owner);
    List<Announcement> findAllByType(String type);
    @Query("SELECT type, count(id) from Announcement" +
            " group by type" +
            " order by count(id) DESC")
    List<Object[]> findMaxByType();

    @Query("SELECT city.name, count(id) from Announcement" +
            " group by city.name" +
            " order by count(id) DESC")
    List<Object[]> findMaxByCity();

    @Query("SELECT SUM(viewed) from Announcement")
    Integer getSum();

    @Query("select id from Announcement where search(:query)=true")
    List<Object[]> fullTextSearch(String query);
}
