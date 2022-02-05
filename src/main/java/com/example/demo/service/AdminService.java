package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.Announcement;
import com.example.demo.model.UserModel;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.MyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final AnnouncementRepository announcementRepository;
    private final MyUtils myUtils;
    public UsersInfoDto getUsers() {

        UsersInfoDto usersInfoDto = new UsersInfoDto();
        List<UserModel> userList = userRepository.findAllByRole("USER").get();
        usersInfoDto.setAllUser(userList.size());
        usersInfoDto.setBannedUser(userRepository.findAllByActive(false).get().size());
        usersInfoDto.setActiveUser(userRepository.findAllByActive(true).get().size());
        List<UserInfoDto> userInfoDtoList = new ArrayList<>();
        for(UserModel user: userList){
            userInfoDtoList.add(new UserInfoDto(user.getLogin(), user.getEmail(), user.getCreatedAt(), user.getActive()));
        }
        usersInfoDto.setUserInfoDtoList(userInfoDtoList);
        return usersInfoDto;
    }

    @Transactional
    public String banUser(String login) {
        UserModel userModel = userRepository.findByLogin(login).get();
        System.out.println(userModel.getActive());
        userModel.setActive(! userModel.getActive());
        System.out.println(userModel.getActive());
        return "OK";
    }

    public StatisticDto getInfo() {
        StatisticDto statisticDto = new StatisticDto();
        statisticDto.setAnnouncementCount(announcementRepository.count());
        statisticDto.setUserCount(userRepository.count());
        List<Object[]> type = announcementRepository.findMaxByType();
        AnnouncementStatDto announcementStatDto = new AnnouncementStatDto();
        announcementStatDto.setValue(type.get(0)[0].toString());
        announcementStatDto.setCount(Integer.parseInt(type.get(0)[1].toString()));
        List<Object[]> cities = announcementRepository.findMaxByCity();
        AnnouncementStatDto announcementStatDto1 = new AnnouncementStatDto();
        announcementStatDto1.setValue(cities.get(0)[0].toString());
        announcementStatDto1.setCount(Integer.parseInt(cities.get(0)[1].toString()));
        statisticDto.setMostAdsType(announcementStatDto);
        statisticDto.setMostCity(announcementStatDto1);
        statisticDto.setViewed(announcementRepository.getSum());
        return statisticDto;
    }

    public List<ResAnnouncementDto> getAnnouncement() {
        List<Announcement> announcementList = announcementRepository.findAll();
        return myUtils.mapList(announcementList);
    }
}
