package com.example.demo.service;

import com.example.demo.dto.AnnouncementDto;
import com.example.demo.dto.ResAnnouncementDto;
import com.example.demo.model.Announcement;
import com.example.demo.model.City;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.repository.CityRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.specification.AnnouncementSpecification;
import com.example.demo.specification.SearchCriteria;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public List<ResAnnouncementDto>get(Map<String, Object> paramMap){
        List<AnnouncementSpecification> announcementSpecifications = new ArrayList<>();
        for(Map.Entry<String, Object > entry: paramMap.entrySet()){
            String key = entry.getKey();
            Object value = entry.getValue();
            String operation;
            if(key.equals("maxPrice")) {
                operation = "<";
                key = "price";
            }
            else if(key.equals("minPrice")) {
                operation = ">";
                key = "price";
            }
            else
                operation = ":";
            announcementSpecifications.add(new AnnouncementSpecification(new SearchCriteria(key, operation, value)));
        }
        Specification<Announcement> specification = Specification.where(announcementSpecifications.get(0));
        for(int i=1; i<announcementSpecifications.size(); i++){
             specification = specification.and(announcementSpecifications.get(i));
        }
        List<Announcement> announcementList = announcementRepository.findAll(specification);
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
