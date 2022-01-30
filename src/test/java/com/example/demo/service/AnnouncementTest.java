package com.example.demo.service;

import com.example.demo.TestConfiguration;
import com.example.demo.dto.AnnouncementDto;
import com.example.demo.dto.ResAnnouncementDto;
import com.example.demo.model.Announcement;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DataJpaTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:testData.sql")
@Import({AnnouncementService.class, TestConfiguration.class})
public class AnnouncementTest {
    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void addTest(){
        String login = userRepository.getOne(1).getLogin();
        AnnouncementDto announcementDto = new AnnouncementDto();
        announcementDto.setType("Auto");
        announcementDto.setCity("Warszawa");
        announcementDto.setTitle("test");
        announcementDto.setDescription("desc");
        announcementDto.setPrice(1200F);
        announcementDto.setBrand("BMW");
        announcementDto.setYear(1000);
        announcementDto.setPower(10000);
        announcementDto.setModel("e39");
        announcementDto.setMileage(12000);
        announcementDto.setCapacity(10000);
        announcementDto.setFirstOwner(true);
        announcementDto.setDamaged(false);
        announcementService.create(announcementDto, login);

        List<ResAnnouncementDto> announcementList = announcementService.getUserAnnouncement(login);
        assertEquals(3, announcementList.size());
    }

    @Test
    public void checkOwnershipTest(){
        String login = userRepository.getOne(1).getLogin();
        String fakeLogin = "fake";
        assertTrue(announcementService.checkOwnership(1, login));
        assertFalse(announcementService.checkOwnership(1, fakeLogin));
    }
}
