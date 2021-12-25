package com.example.demo.service;

import com.example.demo.dto.AnnouncementDto;
import com.example.demo.dto.ResAnnouncementDto;
import com.example.demo.model.Announcement;
import com.example.demo.model.City;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.repository.CityRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final CityRepository cityRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ResAnnouncementDto create(AnnouncementDto announcementDto, String owner) {
        Announcement announcement = new Announcement();
        announcement.setTitle(announcementDto.getTitle());
        announcement.setDescription(announcementDto.getDescription());
        announcement.setType(announcementDto.getType());
        announcement.setOwner(userRepository.findByLogin(owner).get());
        City city = cityRepository.findByName(announcementDto.getCity()).get();
        announcement.setCity(city);
        announcementRepository.save(announcement);
        return modelMapper.map(announcement, ResAnnouncementDto.class);
    }

    public List<ResAnnouncementDto> getUserAnnouncement(String owner){
        List<Announcement> announcementList= announcementRepository.findAllByOwnerLogin(owner);
        return mapList(announcementList);
    }

    public List<ResAnnouncementDto> getAnnouncementByType(String type){
        List<Announcement> announcementList = announcementRepository.findAllByType(type);
        return mapList(announcementList);
    }

    private List<ResAnnouncementDto> mapList(List<Announcement> announcementList){
        List<ResAnnouncementDto> resAnnouncementDtoList = new ArrayList<>();
        for(Announcement announcement: announcementList){
            resAnnouncementDtoList.add(modelMapper.map(announcement, ResAnnouncementDto.class));
        }
        return resAnnouncementDtoList;
    }
}
