package com.example.demo.controller;
import com.example.demo.service.CityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/city")
@AllArgsConstructor
public class CityController {
    private final CityService cityService;

    @GetMapping("/get")
    public ResponseEntity<List<String>> get(){
        return ResponseEntity.ok(cityService.get());
    }
}
