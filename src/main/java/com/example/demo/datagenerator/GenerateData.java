package com.example.demo.datagenerator;

import com.example.demo.model.Announcement;
import com.example.demo.model.City;
import com.example.demo.model.UserModel;
import com.example.demo.model.Voivodeship;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.repository.CityRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VoivodeshipRepository;
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

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public void generateCityAndVoivodeship(){
        JSONArray jsonArray = null;
        try {
            JSONParser jsonParser = new JSONParser();
            Resource resource = new ClassPathResource("cityAndVoivodeship.json");
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new BufferedReader(new InputStreamReader(resource.getInputStream())));
            jsonArray = (JSONArray) jsonParser.parse(jsonObject.get("data").toString());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

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

    public void generateAnnouncement(){
        List<String> cities = List.of("Warszawa" ,"Kielce", "Krakow", "Krakow");
        for(String city: cities){
            Announcement announcement = new Announcement();
            announcement.setOwner(userRepository.findByLogin("login").get());
            announcement.setType("CAR");
            announcement.setTitle("dsadasdas");
            announcement.setDescription("dsaadsdasdsadasdsa");
            announcement.setPrice(new Random().nextFloat() * 100000);
            announcement.setCity(cityRepository.findByName(city).get());
            announcementRepository.save(announcement);
        }
    }

    public void generateUser(){
        UserModel userModel = new UserModel();
        userModel.setActive(true);
        userModel.setCreatedAt(LocalDateTime.now().toString());
        userModel.setEmail("email@email.com");
        userModel.setLogin("login");
        userModel.setPassword(passwordEncoder.encode("password"));
        userRepository.save(userModel);
    }

}
