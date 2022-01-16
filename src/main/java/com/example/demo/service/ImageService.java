package com.example.demo.service;

import com.example.demo.model.Announcement;
import com.example.demo.model.Image;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.repository.ImageRepository;
import com.example.demo.utils.MyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final AnnouncementRepository announcementRepository;
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
    public Image get() throws DataFormatException {
        Image image = imageRepository.getOne(1);
        image.setBytes(myUtils.decompressBytes(image.getBytes()));
        return image;
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

}
