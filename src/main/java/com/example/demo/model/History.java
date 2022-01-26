package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private UserModel owner;
    @ManyToMany()
    private List<Announcement> announcements = new ArrayList<>();

    public void add(Announcement announcement){
        announcements.add(0, announcement);
    }
}
