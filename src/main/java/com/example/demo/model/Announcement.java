package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Setter
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String type;

    private Float price;

    @ManyToOne
    private Brand brand;

    @ManyToOne
    private UserModel owner;

    @ManyToOne
    private City city;

    private String title;

    private String description;

    private Integer viewed;

    public void increaseViewed(){
        this.viewed ++;
    }



}
