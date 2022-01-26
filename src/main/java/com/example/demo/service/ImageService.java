package com.example.demo.service;

import com.example.demo.dto.ResImageDto;
import com.example.demo.model.Announcement;
import com.example.demo.model.Image;
import com.example.demo.model.UserModel;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.MyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;
    private final MyUtils myUtils;
    @Transactional
    public String upload(MultipartFile file, Integer id) throws IOException {
        Announcement announcement = announcementRepository.getOne(id);
        Image image = new Image();
        image.setAnnouncement(announcement);
        image.setName("sdsaasd");
        image.setBytes(compressBytes(file.getBytes()));
        announcement.addImage(image);
        imageRepository.saveAndFlush(image);
        return "Ok";


    }
    public List<ResImageDto> get(Integer id) throws DataFormatException {
        List<Image> list = announcementRepository.getOne(id).getImages();
        List<ResImageDto> resImageDtos = new ArrayList<>();
        for (Image image : list) {
            resImageDtos.add(new ResImageDto(image.getId(), myUtils.decompressBytes(image.getBytes())));
        }
        return resImageDtos;
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
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

        return outputStream.toByteArray();
    }

    @Transactional
    public String delete(Integer id, String login) {
        Image image = imageRepository.getOne(id);
        userRepository.findByLogin(login).get().getAnnouncements().remove(image);
        imageRepository.delete(image);
        return "";
    }
}
