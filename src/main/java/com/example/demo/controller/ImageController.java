package com.example.demo.controller;

import com.example.demo.dto.ResImageDto;
import com.example.demo.model.Image;
import com.example.demo.service.ImageService;
import com.example.demo.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor

public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam Integer id, @RequestParam("imageFile") MultipartFile file) throws IOException {
        return ResponseEntity.ok(imageService.upload(file, id));
    }

    @GetMapping("/get")
    public ResponseEntity<List<ResImageDto>> getImage (@RequestParam Integer id) throws DataFormatException {
        return ResponseEntity.ok(imageService.get(id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteImage(@RequestParam Integer id){
        return ResponseEntity.ok(imageService.delete(id, JwtUtils.getUsernameFromHeader()));

    }
}
