package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UsersInfoDto {
    private Integer allUser;
    private Integer activeUser;
    private Integer bannedUser;
    List<UserInfoDto> userInfoDtoList;
}
