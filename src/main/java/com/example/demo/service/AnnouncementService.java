package com.example.demo.service;

import com.example.demo.dto.AnnouncementDto;
import com.example.demo.dto.ResAnnouncementDto;
import com.example.demo.model.Announcement;
import com.example.demo.model.City;
import com.example.demo.model.History;
import com.example.demo.repository.*;
import com.example.demo.specification.AnnouncementSpecification;
import com.example.demo.specification.SearchCriteria;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final CityRepository cityRepository;
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final ModelMapper modelMapper;

    public ResAnnouncementDto create(AnnouncementDto announcementDto, String owner) {
        Announcement announcement = new Announcement();
        announcement.setTitle(announcementDto.getTitle());
        announcement.setPrice(announcementDto.getPrice());
        announcement.setDescription(announcementDto.getDescription());
        announcement.setType(announcementDto.getType());
        announcement.setOwner(userRepository.findByLogin(owner).get());
        City city = cityRepository.findByName(announcementDto.getCity()).get();
        announcement.setCity(city);
        announcement.setViewed(0);
        announcement.setBrand(brandRepository.findByName(announcementDto.getBrand()).get());
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

    @Transactional
    public ResAnnouncementDto edit(Integer id, Map<String, Object> paramsMap) {
        Announcement announcement = announcementRepository.findById(id).get();
        for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            switch (key) {
                case "price":
                    announcement.setPrice(Float.parseFloat(String.valueOf(value)));
                    System.out.println("price = " + value.toString());
                    break;
                case "city":
                    announcement.setCity((City) value);
                    break;
                case "title":
                    announcement.setTitle((String) value);
                    break;
                case "description":
                    announcement.setDescription((String) value);
                    break;
            }
        }
        return modelMapper.map(announcement, ResAnnouncementDto.class);
    }

    @Transactional
    public ResAnnouncementDto add_viewed(Integer id) {
        Announcement announcement = announcementRepository.getOne(id);
        announcement.increaseViewed();
        return modelMapper.map(announcement, ResAnnouncementDto.class);
    }

    public ResAnnouncementDto getById(Integer id) {
        Announcement announcement = announcementRepository.getOne(id);
        return modelMapper.map(announcement, ResAnnouncementDto.class);
    }
}
