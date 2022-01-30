package com.example.demo.service;

import com.example.demo.TestConfiguration;
import com.example.demo.dto.ResAnnouncementDto;
import com.example.demo.model.Announcement;
import com.example.demo.model.Favourite;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:testData.sql")
@Import({FavouriteService.class, TestConfiguration.class})

public class FavouriteTest {
    @Autowired
    private FavouriteService favouriteService;
    @Autowired
    private AnnouncementRepository announcementRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void favouriteTest(){
        Integer announcementId = 1;
        String login = userRepository.getOne(1).getLogin();
        List<ResAnnouncementDto> list = favouriteService.get(login);
        assertEquals(0, list.size());

        favouriteService.toggle(login, announcementId);
        list = favouriteService.get(login);
        assertEquals(1, list.size());
        favouriteService.toggle(login, announcementId);
        list = favouriteService.get(login);
        assertEquals(0, list.size());
    }



}
