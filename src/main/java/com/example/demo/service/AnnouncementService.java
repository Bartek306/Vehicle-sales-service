package com.example.demo.service;

import com.example.demo.dto.AnnouncementDto;
import com.example.demo.dto.ResAnnouncementDto;
import com.example.demo.model.Announcement;
import com.example.demo.model.City;
import com.example.demo.model.History;
import com.example.demo.model.Image;
import com.example.demo.repository.*;
import com.example.demo.specification.AnnouncementSpecification;
import com.example.demo.specification.SearchCriteria;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        announcement.setYear(announcementDto.getYear());
        announcement.setModel(announcementDto.getModel());
        announcement.setPower(announcementDto.getPower());
        announcement.setMileage(announcementDto.getMileage());
        announcement.setDamaged(announcementDto.isDamaged());
        announcement.setFirstOwner(announcementDto.isFirstOwner());
        announcementRepository.save(announcement);
        return modelMapper.map(announcement, ResAnnouncementDto.class);
    }

    public List<ResAnnouncementDto> getUserAnnouncement(String owner){
        List<Announcement> announcementList= announcementRepository.findAllByOwnerLogin(owner);
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
        ResAnnouncementDto  resAnnouncementDto =  modelMapper.map(announcement, ResAnnouncementDto.class);
        try {
            resAnnouncementDto.setImageBytes(decompressBytes(resAnnouncementDto.getImageBytes()));
        } catch (DataFormatException e) {
            e.printStackTrace();
        }
        return resAnnouncementDto;    }

    @Transactional
    public ResAnnouncementDto addViewed(Integer id) {
        Announcement announcement = announcementRepository.getOne(id);
        announcement.increaseViewed();
        ResAnnouncementDto  resAnnouncementDto =  modelMapper.map(announcement, ResAnnouncementDto.class);
        if(announcement.getImage() == null){
            resAnnouncementDto.setImageBytes(imageRepository.getOne(1).getBytes());
        }
        try {
            resAnnouncementDto.setImageBytes(decompressBytes(resAnnouncementDto.getImageBytes()));
        } catch (DataFormatException e) {
            e.printStackTrace();
        }
        return resAnnouncementDto;    }

    public ResAnnouncementDto getById(Integer id) {
        Announcement announcement = announcementRepository.getOne(id);
        ResAnnouncementDto  resAnnouncementDto =  modelMapper.map(announcement, ResAnnouncementDto.class);
        if(announcement.getImage() == null){
            resAnnouncementDto.setImageBytes(imageRepository.getOne(1).getBytes());
        }
        try {
            resAnnouncementDto.setImageBytes(decompressBytes(resAnnouncementDto.getImageBytes()));
        } catch (DataFormatException e) {
            e.printStackTrace();
        }
        return resAnnouncementDto;
    }

    public Boolean checkOwnership(Integer id, String owner) {
        Announcement announcement = announcementRepository.getOne(id);
        return announcement.getOwner().equals(userRepository.findByLogin(owner).get());
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
