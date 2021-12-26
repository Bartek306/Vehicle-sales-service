package com.example.demo.service;

import com.example.demo.model.Brand;
import com.example.demo.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    public Brand getBrandFromName(String name){
        return brandRepository.findByName(name).get();
    }
}
