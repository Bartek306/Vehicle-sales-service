package com.example.demo.dto;

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
    private Float price;
}
