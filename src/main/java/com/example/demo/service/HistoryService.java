package com.example.demo.service;

import com.example.demo.dto.ResAnnouncementDto;
import com.example.demo.model.Announcement;
import com.example.demo.model.History;
import com.example.demo.model.UserModel;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.repository.HistoryRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service

public class HistoryService {

    private final ModelMapper modelMapper;
    private final HistoryRepository historyRepository;
    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;

    @Transactional
    public void add(String login, Integer announcementId){
        History history = historyRepository.findByOwnerLogin(login).get();
        UserModel userModel = userRepository.findByLogin(login).get();
        Announcement announcement = announcementRepository.getOne(announcementId);
        if(! history.getAnnouncements().contains(announcement) && announcement.getOwner() != userModel){
            history.add(announcement);
        }
    }
    public List<ResAnnouncementDto> get(String userId) {
        History history = historyRepository.findByOwnerLogin(userId).get();
        List<Announcement> list = history.getAnnouncements();
        List<ResAnnouncementDto> resAnnouncementDtoList = new ArrayList<>();
        for(Announcement announcement: list){
            resAnnouncementDtoList.add(modelMapper.map(announcement, ResAnnouncementDto.class));
        }
        return resAnnouncementDtoList;
    }
}
