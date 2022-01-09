package com.example.demo.service;

import com.example.demo.dto.ResAnnouncementDto;
import com.example.demo.model.Announcement;
import com.example.demo.model.History;
import com.example.demo.model.UserModel;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.repository.HistoryRepository;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.UserRepository;
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

    private final ModelMapper modelMapper;
    private final HistoryRepository historyRepository;
    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

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
        return mapList(list);
    }

    private List<ResAnnouncementDto> mapList(List<Announcement> announcementList){
        List<ResAnnouncementDto> resAnnouncementDtoList = new ArrayList<>();
        for(int i=0; i<announcementList.size(); i++){
            resAnnouncementDtoList.add(modelMapper.map(announcementList.get(i), ResAnnouncementDto.class));
            try {
                if(announcementList.get(i).getImage() != null)
                    resAnnouncementDtoList.get(i).setImageBytes(decompressBytes(announcementList.get(i).getImage().getBytes()));
                else
                    resAnnouncementDtoList.get(i).setImageBytes(decompressBytes(imageRepository.getOne(1).getBytes()));
            } catch (DataFormatException e) {
                e.printStackTrace();
            }

        }
        return resAnnouncementDtoList;
    }

    private byte[] decompressBytes(byte[] data) throws DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }
}
