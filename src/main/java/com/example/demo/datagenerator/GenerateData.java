package com.example.demo.datagenerator;

import com.example.demo.model.City;
import com.example.demo.model.Voivodeship;
import com.example.demo.repository.CityRepository;
import com.example.demo.repository.VoivodeshipRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GenerateData {
    private final VoivodeshipRepository voivodeshipRepository;
    private final CityRepository cityRepository;
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
}
