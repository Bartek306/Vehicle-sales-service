package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticDto {
    private Long announcementCount;
    private Long userCount;
    private AnnouncementStatDto mostAdsType;
    private AnnouncementStatDto mostCity;
    private Integer viewed;

}
