package com.example.demo.service;

import com.example.demo.model.Announcement;
import com.example.demo.model.Image;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final AnnouncementRepository announcementRepository;

    @Transactional
    public String upload(MultipartFile file, Integer id) throws IOException {
        Announcement announcement = announcementRepository.getOne(id);
        Image image = new Image();
        image.setAnnouncement(announcement);
        image.setName("sdsaasd");
        image.setBytes(compressBytes(file.getBytes()));
        imageRepository.save(image);
        return "Ok";


    }

    public Image get(Integer id) throws DataFormatException {
        Image image;
        try {
            image = imageRepository.findImageByAnnouncementId(id).get();
        }catch (Exception e){
            image = imageRepository.getOne(1);
        }
        Image img = new Image();
        img.setName(image.getName());
        img.setBytes(decompressBytes(image.getBytes()));
        return img;
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

    private byte[] decompressBytes(byte[] data) throws DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }
}
