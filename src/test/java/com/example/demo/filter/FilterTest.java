package com.example.demo.filter;

import com.example.demo.TestConfiguration;
import com.example.demo.dto.ResAnnouncementDto;
import com.example.demo.repository.CityRepository;
import com.example.demo.service.AnnouncementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:testData.sql")
@Import({AnnouncementService.class, TestConfiguration.class})

public class FilterTest {
    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private CityRepository cityRepository;

    @Test
    public void searchAnnouncementBetween1992and2000ShouldReturn2(){
        Map<String, Object> map = new HashMap<>();
        map.put("maxYear", 2000);
        map.put("minYear", 1992);
        List<ResAnnouncementDto> list = announcementService.get(map);
        assertEquals(2, list.size());
    }

    @Test
    public void searchAnnouncementFromWarszawaShouldReturn4(){
        Map<String, Object> map = new HashMap<>();
        map.put("city", cityRepository.findByName("Warszawa").get());
        List<ResAnnouncementDto> list = announcementService.get(map);
        assertEquals(4, list.size());
    }

    @Test
    public void searchAnnouncementWithFirstOwnerShouldReturn6(){
        Map<String, Object> map = new HashMap<>();
        map.put("firstOwner", true);
        List<ResAnnouncementDto> list = announcementService.get(map);
        assertEquals(6, list.size());
    }

    @Test
    public void searchAnnouncementWithMinPrice10000AndFromWarszawaShouldReturn3(){
        Map<String, Object> map = new HashMap<>();
        map.put("minPrice", 10000);
        map.put("city", cityRepository.findByName("Warszawa").get());
        List<ResAnnouncementDto> list = announcementService.get(map);
        assertEquals(3, list.size());
    }

    @Test
    public void searchAnnouncementWithMinPrice10000AndMaxPrice50000AndFromKielceAndDamagedShouldReturn2(){
        Map<String, Object> map = new HashMap<>();
        map.put("minPrice", 10000);
        map.put("maxPrice", 50000);
        map.put("city", cityRepository.findByName("Kielce").get());
        map.put("damaged", true);
        List<ResAnnouncementDto> list = announcementService.get(map);
        assertEquals(2, list.size());
    }


}
