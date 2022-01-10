package com.example.demo.service;

import com.example.demo.dto.ResAnnouncementDto;
import com.example.demo.model.Announcement;
import com.example.demo.model.History;
import com.example.demo.model.UserModel;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.repository.HistoryRepository;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.MyUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

@RequiredArgsConstructor
@Service

public class HistoryService {

    private final HistoryRepository historyRepository;
    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;
    private final MyUtils myUtils;
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
        return myUtils.mapList(list);
    }
}
