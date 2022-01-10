package com.example.demo.controller;

import com.example.demo.dto.AnnouncementDto;
import com.example.demo.dto.ResAnnouncementDto;
import com.example.demo.service.AnnouncementService;
import com.example.demo.service.BrandService;
import com.example.demo.service.CityService;
import com.example.demo.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/announcement")
@RequiredArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;
    private final CityService cityService;
    private final BrandService brandService;

    @PostMapping("/create")
    public ResponseEntity<ResAnnouncementDto> create(@RequestBody AnnouncementDto announcementDto){
        return ResponseEntity.ok(announcementService.create(announcementDto, JwtUtils.getUsernameFromHeader()));
    }

    @GetMapping("/user")
    public ResponseEntity<List<ResAnnouncementDto>> getUserAnnouncement(){
        return ResponseEntity.ok(announcementService.getUserAnnouncement(JwtUtils.getUsernameFromHeader()));
    }
    @GetMapping("/get_by_id")
    public ResponseEntity<ResAnnouncementDto> getById(@RequestParam Integer id){
        return ResponseEntity.ok(announcementService.getById(id));
    }
    @GetMapping("/get")
    public ResponseEntity<List<ResAnnouncementDto>> get(@RequestParam(required = false) String city,
                                                        @RequestParam(required = false) String type,
                                                        @RequestParam(required = false) String maxPrice,
                                                        @RequestParam(required = false) String minPrice,
                                                        @RequestParam(required = false) String brand) {
        Map<String, Object> paramsMap = new HashMap<>();
        addToMap("city", cityService.getCityFromName(city), paramsMap);
        addToMap("type", type, paramsMap);
        addToMap("maxPrice", maxPrice, paramsMap);
        addToMap("minPrice", minPrice, paramsMap);
        addToMap("brand", brandService.getBrandFromName(brand), paramsMap);
        return ResponseEntity.ok(announcementService.get(paramsMap));
    }

    @PutMapping("/edit")
    public ResponseEntity<ResAnnouncementDto> edit(@RequestParam Integer id, @RequestBody AnnouncementDto announcementDto ){
        return ResponseEntity.ok(announcementService.edit(id, announcementDto));

    }

    @PutMapping("/add_viewed")
    public ResponseEntity<String> addViewed(@RequestParam Integer id){
        return ResponseEntity.ok(announcementService.addViewed(id));
    }

    @GetMapping("check_ownership")
    public ResponseEntity<Boolean> checkOwnership(@RequestParam Integer id){
        return ResponseEntity.ok(announcementService.checkOwnership(id, JwtUtils.getUsernameFromHeader()));
    }


    private static void addToMap(String key, Object value, Map<String, Object> map){
    if(value != null){
        map.put(key, value);
    }
    }

}