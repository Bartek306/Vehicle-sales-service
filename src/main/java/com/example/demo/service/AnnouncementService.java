package com.example.demo.service;

import com.example.demo.dto.AnnouncementDto;
import com.example.demo.dto.ResAnnouncementDto;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.specification.AnnouncementSpecification;
import com.example.demo.specification.SearchCriteria;
import com.example.demo.utils.MyUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final CityRepository cityRepository;
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final ImageRepository imageRepository;
    private final HistoryRepository historyRepository;
    private final FavouriteRepository favouriteRepository;
    private final ModelMapper modelMapper;
    private final MyUtils myUtils;

    public ResAnnouncementDto create(AnnouncementDto announcementDto, String owner) {
        Announcement announcement = new Announcement();
        announcement.setTitle(announcementDto.getTitle());
        announcement.setPrice(announcementDto.getPrice());
        announcement.setDescription(announcementDto.getDescription());
        announcement.setType(announcementDto.getType());
        announcement.setOwner(userRepository.findByLogin(owner).get());
        announcement.setCity( cityRepository.findByName(announcementDto.getCity()).get());
        announcement.setViewed(0);
        announcement.setBrand(brandRepository.findByName(announcementDto.getBrand()).get());
        announcement.setYear(announcementDto.getYear());
        announcement.setModel(announcementDto.getModel());
        announcement.setPower(announcementDto.getPower());
        announcement.setMileage(announcementDto.getMileage());
        announcement.setCapacity(announcementDto.getCapacity());
        announcement.setDamaged(announcementDto.isDamaged());
        announcement.setFirstOwner(announcementDto.isFirstOwner());
        announcementRepository.save(announcement);
        return modelMapper.map(announcement, ResAnnouncementDto.class);
    }

    public List<ResAnnouncementDto> getUserAnnouncement(String owner){
        List<Announcement> announcementList= announcementRepository.findAllByOwnerLogin(owner);
        return myUtils.mapList(announcementList);
    }


    public List<ResAnnouncementDto>get(Map<String, Object> paramMap){
        if(paramMap.get("query") != null){
            return search(paramMap.get("query").toString());
        }
        List<AnnouncementSpecification> announcementSpecifications = new ArrayList<>();
        for(Map.Entry<String, Object > entry: paramMap.entrySet()){
            String key = entry.getKey();
            Object value = entry.getValue();
            String operation;
            if(key.equals("maxPrice")) {
                operation = "<";
                key = "price";
            }
            else if(key.equals("maxYear")){
                operation = "<";
                key = "year";
            }
            else if(key.equals("minYear")){
                operation = ">";
                key = "year";
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
        return myUtils.mapList(announcementList);
    }

    @Transactional
    public ResAnnouncementDto edit(Integer id, AnnouncementDto announcementDto) {
        Announcement announcement = announcementRepository.findById(id).get();
        announcement.setModel(announcementDto.getModel());
        announcement.setPower(announcementDto.getPower());
        announcement.setYear(announcementDto.getYear());
        announcement.setFirstOwner(announcementDto.isFirstOwner());
        announcement.setType(announcementDto.getType());
        announcement.setCity(cityRepository.findByName(announcementDto.getCity()).get());
        announcement.setTitle(announcementDto.getTitle());
        announcement.setDescription(announcementDto.getDescription());
        announcement.setPrice(announcementDto.getPrice());
        announcement.setBrand(brandRepository.findByName(announcementDto.getBrand()).get());
        announcement.setMileage(announcementDto.getMileage());
        announcement.setFirstOwner(announcementDto.isFirstOwner());
        announcement.setDamaged(announcementDto.isDamaged());
        return modelMapper.map(announcement, ResAnnouncementDto.class);
    }

    @Transactional
    public String addViewed(Integer id) {
        Announcement announcement = announcementRepository.getOne(id);
        announcement.increaseViewed();
        System.out.println(announcement.getViewed());
        return "OK";
    }

    public ResAnnouncementDto getById(Integer id) {
        Announcement announcement = announcementRepository.getOne(id);
        ResAnnouncementDto  resAnnouncementDto =  modelMapper.map(announcement, ResAnnouncementDto.class);
        if(announcement.getImages().isEmpty()){
            try {
                resAnnouncementDto.addToImageBytes(myUtils.decompressBytes(imageRepository.getOne(1).getBytes()));
            } catch (DataFormatException e) {
                e.printStackTrace();
            }
        }
        else {
            for(int i =0; i< announcement.getImages().size(); i++){
                try {
                    resAnnouncementDto.getImagesBytes().add(myUtils.decompressBytes(announcement.getImages().get(i).getBytes()));
                } catch (DataFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        return resAnnouncementDto;
    }

    public Boolean checkOwnership(Integer id, String owner) {
        Announcement announcement = announcementRepository.getOne(id);
        UserModel userModel;
        try {
           userModel = userRepository.findByLogin(owner).get();
        }catch (Exception e){
            return false;
        }
        return announcement.getOwner().equals(userModel);
    }
    @Transactional
    public ResAnnouncementDto delete(Integer id) {
        Announcement announcement = announcementRepository.getOne(id);
        List<Image> images = announcement.getImages();
        for (Image image : images) {
            image.setAnnouncement(null);
        }
        announcementRepository.delete(announcement);
        List<History> histories = historyRepository.findAll();
        for(History history: histories){
            history.remove(announcement);
        }
        List<Favourite> favourites = favouriteRepository.findAll();
        for(Favourite favourite: favourites){
            favourite.remove(announcement);
        }
        return modelMapper.map(announcement, ResAnnouncementDto.class);
    }

    public List<ResAnnouncementDto>search(String value){
        List<Announcement> announcementList = announcementRepository.findAll();
        Set<Announcement> set = new HashSet<>();
        for(Announcement announcement: announcementList){
            if(announcement.getTitle().contains(value)){
                set.add(announcement);
            }
        }
        List<Announcement> listFromSet = new ArrayList<>(set);
        return myUtils.mapList(listFromSet);
    }
}
