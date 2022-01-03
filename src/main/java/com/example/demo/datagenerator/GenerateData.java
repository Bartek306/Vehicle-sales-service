package com.example.demo.datagenerator;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class GenerateData {
    private final VoivodeshipRepository voivodeshipRepository;
    private final CityRepository cityRepository;
    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final HistoryRepository historyRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public void generateCityAndVoivodeship(){
        JSONArray jsonArray = getArray("cityAndVoivodeship.json");

        for (Object o : Objects.requireNonNull(jsonArray)) {
            JSONObject jsonObject = (JSONObject) o;
            City city = new City();
            Optional<Voivodeship> voivodeship = voivodeshipRepository.findByName(jsonObject.get("voivodeship").toString());
            if(voivodeship.isPresent()){
                city.setVoivodeship(voivodeship.get());
            }
            else {
                Voivodeship newVoivodeship = new Voivodeship();
                newVoivodeship.setName(jsonObject.get("voivodeship").toString());
                voivodeshipRepository.save(newVoivodeship);
                city.setVoivodeship(newVoivodeship);
            }
            city.setName(jsonObject.get("city").toString());
            cityRepository.save(city);
        }
    }

    private JSONArray getArray(String filename){
        JSONArray jsonArray = null;
        try {
            JSONParser jsonParser = new JSONParser();
            Resource resource = new ClassPathResource(filename);
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new BufferedReader(new InputStreamReader(resource.getInputStream())));
            jsonArray = (JSONArray) jsonParser.parse(jsonObject.get("data").toString());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public void generateAnnouncement(){
        List<String> cities = List.of("Warszawa" ,"Kielce", "Krakow", "Krakow");
        List<Brand> brands = brandRepository.findAll();
        Random random = new Random();
        for(String city: cities){
            Announcement announcement = new Announcement();
            announcement.setOwner(userRepository.findByLogin("login").get());
            announcement.setType("CAR");
            announcement.setTitle("dsadasdas");
            announcement.setDescription("dsaadsdasdsadasdsa");
            announcement.setBrand(brands.get(random.nextInt(brands.size())));
            announcement.setPrice(random.nextFloat() * 100000);
            announcement.setCity(cityRepository.findByName(city).get());
            announcementRepository.save(announcement);
        }
    }

    public void generateUser(){
        UserModel userModel = new UserModel();
        History history = new History();
        userModel.setActive(true);
        userModel.setCreatedAt(LocalDateTime.now().toString());
        userModel.setEmail("email@email.com");
        userModel.setLogin("login");
        userModel.setPassword(passwordEncoder.encode("password"));
        userModel.setHistory(history);
        history.setOwner(userModel);
        historyRepository.save(history);
        userRepository.saveAndFlush(userModel);
    }

    public void generateBrand(){
        JSONArray jsonArray = getArray("brand.json");
        for(Object o: Objects.requireNonNull(jsonArray)){
            JSONObject jsonObject = (JSONObject) o;
            Brand brand = new Brand();
            brand.setName(jsonObject.get("name").toString());
            brandRepository.save(brand);
        }
    }

    public void generateHistory(){
        Announcement announcement = announcementRepository.findById(1).get();
        Announcement announcement1 = announcementRepository.findById(2).get();
        UserModel userModel = userRepository.findByLogin("login").get();
        System.out.println(userModel.getHistory());
        userModel.getHistory().add(announcement);
        userModel.getHistory().add(announcement1);

    }
}
