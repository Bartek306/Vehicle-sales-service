package com.example.demo.service;

import com.example.demo.model.Brand;
import com.example.demo.model.City;
import com.example.demo.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    public Brand getBrandFromName(String name){
        return brandRepository.findByName(name).orElse(null);
    }

    public List<String> get() {
        List<Brand> brandList = brandRepository.findAll();
        List<String> names = new ArrayList<>();
        for(Brand brand: brandList){
            names.add(brand.getName());
        }
        return names;
    }
}
