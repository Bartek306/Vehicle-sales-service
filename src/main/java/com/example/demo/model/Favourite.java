package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Favourite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private UserModel owner;

    @ManyToMany
    private List<Announcement> announcements = new ArrayList<>();

    public void add(Announcement announcement){
        announcements.add(announcement);
    }

    public void remove(Announcement announcement){
        announcements.remove(announcement);
    }
}
