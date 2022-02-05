package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Announcement")
@Setter
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition="TEXT", length = 1000)
    private String type;

    private Float price;

    @ManyToOne
    private Brand brand;

    @ManyToOne
    @JsonIgnore
    private UserModel owner;

    @OneToMany
    @JsonIgnore
    @Column(length = 16000000)
    private List<Image> images = new ArrayList<>();

    @ManyToOne
    private City city;

    private String title;
    @Column(columnDefinition="TEXT", length = 1000)
    private String description;

    private Integer viewed;

    private Integer year;

    private String model;

    @Column(nullable = true)
    private Integer power;

    @Column(nullable = true)
    private Integer mileage;

    @Column(nullable = true)
    private Integer capacity;

    @Column(nullable = false)
    private boolean firstOwner = false;

    private boolean damaged = false;

    public void increaseViewed(){
        this.viewed ++;
    }

    public void addImage(Image image){
        images.add(image);
    }



}
