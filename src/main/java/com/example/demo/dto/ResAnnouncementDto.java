package com.example.demo.dto;

import com.example.demo.model.Brand;
import com.example.demo.model.Image;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ResAnnouncementDto{
    private Integer announcementId;
    private String ownerLogin;
    private String ownerEmail;
    private String city;
    private String title;
    private String description;
    private List<byte []> imagesBytes = new ArrayList<>();
    private String model;
    private Integer year;
    private Float price;
    private Integer mileage;
    private Brand brand;
    private String type;
    private Integer viewed;

    public void addToImageBytes(byte [] imageByte){
        imagesBytes.add(imageByte);
    }

}
