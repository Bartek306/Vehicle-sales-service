package com.example.demo.controller;

import com.example.demo.service.BrandService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/brand")
@AllArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @GetMapping("/get")
    public ResponseEntity<List<String>> get(){
        return ResponseEntity.ok(brandService.get());
    }

}
