package com.example.demo.service;

import com.example.demo.model.City;
import com.example.demo.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    public City getCityFromName(String name){
        return cityRepository.findByName(name).orElse(null);
    }

    public List<String> get() {
        List<City> cityList = cityRepository.findAll();
        List<String> names = new ArrayList<>();
        for(City city: cityList){
            names.add(city.getName());
        }
        return names;
    }
}
