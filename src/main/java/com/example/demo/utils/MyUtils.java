package com.example.demo.utils;

import com.example.demo.dto.ResAnnouncementDto;
import com.example.demo.model.Announcement;
import com.example.demo.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

@Component
@RequiredArgsConstructor
public class MyUtils {
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    public List<ResAnnouncementDto> mapList(List<Announcement> announcementList){
        List<ResAnnouncementDto> resAnnouncementDtoList = new ArrayList<>();
        for(int i=0; i<announcementList.size(); i++){
            resAnnouncementDtoList.add(modelMapper.map(announcementList.get(i), ResAnnouncementDto.class));
            try {
                if(announcementList.get(i).getImage() != null)
                    resAnnouncementDtoList.get(i).setImageBytes(decompressBytes(announcementList.get(i).getImage().getBytes()));
                else
                    resAnnouncementDtoList.get(i).setImageBytes(decompressBytes(imageRepository.getOne(1).getBytes()));
            } catch (DataFormatException e) {
                e.printStackTrace();
            }

        }
        return resAnnouncementDtoList;
    }

    public byte[] decompressBytes(byte[] data) throws DataFormatException {
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
        } catch (IOException | DataFormatException ioe) {
        }
        return outputStream.toByteArray();
    }
}
