package com.example.demo.dto;

import com.example.demo.model.Brand;
import com.example.demo.model.Image;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResAnnouncementDto{
    private Integer announcementId;
    private String ownerLogin;
    private String ownerEmail;
    private String city;
    private String title;
    private String description;
    private Image image;
    private String model;
    private Integer year;
    private Float price;
    private Integer mileage;
    private Brand brand;
    private String type;
    private Integer viewed;


}
