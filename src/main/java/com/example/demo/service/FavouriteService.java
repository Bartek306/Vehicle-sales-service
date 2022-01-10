package com.example.demo.service;

import com.example.demo.dto.ResAnnouncementDto;
import com.example.demo.model.Announcement;
import com.example.demo.model.Favourite;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.repository.FavouriteRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.MyUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavouriteService {
    private final ModelMapper modelMapper;
    private final AnnouncementRepository announcementRepository;
    private final FavouriteRepository favouriteRepository;
    private final MyUtils myUtils;
    @Transactional
    public void toggle(String login, Integer id) {
        Favourite favourite = favouriteRepository.findByOwnerLogin(login).get();
        Announcement announcement = announcementRepository.getOne(id);
        if(favourite.getAnnouncements().contains(announcement)){
            favourite.remove(announcement);
        }
        else {
            favourite.add(announcement);
        }
    }

    public List<ResAnnouncementDto> get(String login){
        Favourite favourite = favouriteRepository.findByOwnerLogin(login).get();
        List<Announcement> list = favourite.getAnnouncements();
        return myUtils.mapList(list);
    }

    public Boolean check(Integer id, String login) {
        Announcement announcement = announcementRepository.getOne(id);
        Favourite favourite = favouriteRepository.findByOwnerLogin(login).get();
        return favourite.getAnnouncements().contains(announcement);
    }
}
