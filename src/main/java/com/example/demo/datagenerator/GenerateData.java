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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.zip.Deflater;

@Component
@RequiredArgsConstructor
public class GenerateData {
    private final CityRepository cityRepository;
    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final HistoryRepository historyRepository;
    private final FavouriteRepository favouriteRepository;
    private final ImageRepository imageRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public void generateCityAndVoivodeship(){
        JSONArray jsonArray = getArray("cityAndVoivodeship.json");

        for (Object o : Objects.requireNonNull(jsonArray)) {
            JSONObject jsonObject = (JSONObject) o;
            City city = new City();
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

    public void generateAnnouncement() throws IOException {
        Random random = new Random();
        String baseUrl = "src/main/resources/";
        FileInputStream fileInputStream = new FileInputStream(baseUrl + "error.jpg" );
        MultipartFile multipartFile = new MockMultipartFile("error", "error.jpg",
                "image/jpg", IOUtils.toByteArray(fileInputStream));
        Image errorImage = new Image();
        errorImage.setBytes(compressBytes(multipartFile.getBytes()));
        imageRepository.save(errorImage);

        JSONArray jsonArray = getArray("announcement.json");
        for(Object o: Objects.requireNonNull(jsonArray)){
            JSONObject jsonObject = (JSONObject) o;
            Announcement announcement = new Announcement();
            announcement.setOwner(userRepository.findByLogin(jsonObject.get("owner").toString()).get());
            announcement.setType(jsonObject.get("type").toString());
            announcement.setPrice(Float.parseFloat(jsonObject.get("price").toString()));
            announcement.setBrand(brandRepository.findByName(jsonObject.get("brand").toString()).get());
            announcement.setCity(cityRepository.findByName(jsonObject.get("city").toString()).get());
            announcement.setTitle(jsonObject.get("title").toString());
            announcement.setDescription(jsonObject.get("description").toString());
            announcement.setModel(jsonObject.get("model").toString());
            if(jsonObject.get("power")!= null){
                announcement.setPower(Integer.parseInt(jsonObject.get("power").toString()));
                announcement.setMileage(Integer.parseInt(jsonObject.get("mileage").toString()));
            }
            announcement.setFirstOwner(Boolean.parseBoolean(jsonObject.get("firstOwner").toString()));
            announcement.setDamaged(Boolean.parseBoolean(jsonObject.get("damaged").toString()));
            JSONArray imageArray = (JSONArray) jsonObject.get("images");
            for(Object img: Objects.requireNonNull(imageArray)){
                JSONObject imageObject = (JSONObject) img;
                fileInputStream = new FileInputStream(baseUrl + imageObject.get("name").toString() );
                multipartFile = new MockMultipartFile("error", "error.jpg",
                        "image/jpg", IOUtils.toByteArray(fileInputStream));
                Image image = new Image();
                image.setBytes(compressBytes(multipartFile.getBytes()));
                image.setAnnouncement(announcement);
                announcement.addImage(image);
                imageRepository.save(image);
            }

            announcement.setViewed(0);
            announcementRepository.save(announcement);

        }



    }

    public void generateUser(){
        UserModel userModel = new UserModel();
        History history = new History();
        Favourite favourite = new Favourite();
        userModel.setActive(true);
        userModel.setCreatedAt(LocalDateTime.now().toString());
        userModel.setEmail("email@email.com");
        userModel.setLogin("login");
        userModel.setPassword(passwordEncoder.encode("password"));
        userModel.setHistory(history);
        userModel.setFavourite(favourite);
        history.setOwner(userModel);
        userModel.setPhoneNumber("123456789");
        favourite.setOwner(userModel);
        historyRepository.save(history);
        favouriteRepository.save(favourite);
        userRepository.saveAndFlush(userModel);

        UserModel userModel1 = new UserModel();
        History history1 = new History();
        Favourite favourite1 = new Favourite();
        userModel1.setActive(true);
        userModel1.setCreatedAt(LocalDateTime.now().toString());
        userModel1.setEmail("test@email.com");
        userModel1.setLogin("testowy");
        userModel1.setPassword(passwordEncoder.encode("password"));
        userModel1.setHistory(history1);
        history1.setOwner(userModel1);
        favourite1.setOwner(userModel1);
        historyRepository.save(history1);
        userRepository.saveAndFlush(userModel1);
        favouriteRepository.save(favourite1);
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
    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }

        return outputStream.toByteArray();
    }

}
