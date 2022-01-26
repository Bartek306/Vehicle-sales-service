package com.example.demo.repository;

import com.example.demo.model.City;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RepositoryTest {
    @Autowired private CityRepository cityRepository;
    @Autowired private TestEntityManager entityManager;

    @Test
    public void cityTest(){
        final String cityName = "Warszawa";
        City city = new City();
        city.setName(cityName);
        cityRepository.save(city);
        assertEquals(1, cityRepository.count());
        assertEquals(cityName, cityRepository.findByName(cityName).get().getName());

    }
}
