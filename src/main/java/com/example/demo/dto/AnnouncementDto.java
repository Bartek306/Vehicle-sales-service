package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AnnouncementDto {

    private String type;
    private String city;
    private String title;
    private String description;
    private Float price;

}
