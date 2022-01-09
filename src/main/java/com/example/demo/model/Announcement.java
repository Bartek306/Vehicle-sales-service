package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
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
    @JsonIgnore
    private UserModel owner;

    @OneToOne
    @JsonIgnore
    private Image image;

    @ManyToOne
    private City city;

    private String title;

    private String description;

    private Integer viewed;

    private Integer year;

    private String model;

    @Column(nullable = true)
    private Integer power;

    @Column(nullable = true)
    private Integer mileage;


    private boolean firstOwner = false;

    private boolean damaged = false;

    public void increaseViewed(){
        this.viewed ++;
    }



}
