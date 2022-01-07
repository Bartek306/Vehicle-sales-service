package com.example.demo.controller;

import com.example.demo.model.Image;
import com.example.demo.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResponseEntity<Image> getImage (@RequestParam Integer id) throws DataFormatException {
        return ResponseEntity.ok(imageService.get(id));
    }
}
