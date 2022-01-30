package com.example.demo.repository;

import com.example.demo.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RepositoryTest {
    @Autowired private CityRepository cityRepository;
    @Autowired private TestEntityManager entityManager;
    @Autowired private UserRepository userRepository;
    @Autowired private FavouriteRepository favouriteRepository;
    @Autowired private AnnouncementRepository announcementRepository;
    @Autowired private ImageRepository imageRepository;
    @Test
    public void cityTest(){
        final String cityName = "Warszawa";
        City city = new City();
        city.setName(cityName);
        cityRepository.save(city);
        assertEquals(1, cityRepository.count());
        assertEquals(cityName, cityRepository.findByName(cityName).get().getName());

    }

    @Test
    @Transactional
    public void userTest(){
        final String login = "test";
        UserModel userModel = new UserModel();
        userModel.setLogin(login);
        userModel.setPassword("password");
        userModel.setEmail("Email@test.pl");
        userModel.setCreatedAt("created");
        userRepository.save(userModel);
        UserModel userModel1 = userRepository.findByLogin(login).get();
        assertEquals(userModel, userModel1);


        Favourite favourite = new Favourite();
        favourite.setOwner(userModel);
        userModel.setFavourite(favourite);
        Favourite favourite1 = favouriteRepository.findByOwnerLogin(login).get();
        assertEquals(favourite1, favourite);
    }

    @Test
    public void announcementTest(){
        UserModel userModel = new UserModel();
        String login = "login";
        String type = "Auto";
        userModel.setLogin(login);
        userModel.setPassword("password");
        userModel.setEmail("Email@test.pl");
        userModel.setCreatedAt("created");
        userRepository.save(userModel);
        for(int i=0; i<3; i++){
            Announcement announcement = new Announcement();
            announcement.setOwner(userModel);
            announcement.setType(type);
            announcementRepository.save(announcement);
        }
        List<Announcement> typeList = announcementRepository.findAllByType(type);
        List<Announcement> userList = announcementRepository.findAllByOwnerLogin(login);
        assertEquals(3, userList.size());
        assertEquals(3, typeList.size());

        Image image = new Image();
        imageRepository.save(image);
        Announcement announcement = announcementRepository.getOne(1);
        announcement.addImage(image);
        List<Image> images = announcement.getImages();
        assertEquals(1, images.size());

        announcement.setViewed(0);
        for(int i=0; i<100; i++){
            announcement.increaseViewed();
        }
        assertEquals(100, announcement.getViewed());

    }

}
