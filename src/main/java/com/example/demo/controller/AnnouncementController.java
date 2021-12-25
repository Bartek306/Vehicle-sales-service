package com.example.demo.controller;

import com.example.demo.dto.AnnouncementDto;
import com.example.demo.dto.ResAnnouncementDto;
import com.example.demo.service.AnnouncementService;
import com.example.demo.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/announcement")
@RequiredArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;

    @PostMapping("/create")
    public ResponseEntity<ResAnnouncementDto> create(@RequestBody AnnouncementDto announcementDto){
        return ResponseEntity.ok(announcementService.create(announcementDto, JwtUtils.getUsernameFromHeader()));
    }

    @GetMapping("/user")
    public ResponseEntity<List<ResAnnouncementDto>> getUserAnnouncement(){
        return ResponseEntity.ok(announcementService.getUserAnnouncement(JwtUtils.getUsernameFromHeader()));
    }


    @GetMapping("/type")
    public ResponseEntity<List<ResAnnouncementDto>> getTypeAnnouncement(@RequestParam String type){
        System.out.println(type);
        return ResponseEntity.ok(announcementService.getAnnouncementByType(type));
    }

}
