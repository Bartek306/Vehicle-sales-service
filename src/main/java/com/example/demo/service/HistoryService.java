package com.example.demo.service;

import com.example.demo.dto.ResAnnouncementDto;
import com.example.demo.model.Announcement;
import com.example.demo.model.History;
import com.example.demo.repository.HistoryRepository;
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
    @Transactional
    public void addToHistory(String userId, Announcement announcement){
        History history = historyRepository.findByOwnerLogin(userId).get();
        history.add(announcement);
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
