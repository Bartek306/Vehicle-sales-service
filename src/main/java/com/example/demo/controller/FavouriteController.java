package com.example.demo.controller;

import com.example.demo.dto.ResAnnouncementDto;
import com.example.demo.service.FavouriteService;
import com.example.demo.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favourite")
@AllArgsConstructor
public class FavouriteController {

    private final FavouriteService favouriteService;

    @PostMapping("/toggle")
    public ResponseEntity<String> toggle(@RequestParam Integer id){
        favouriteService.toggle(JwtUtils.getUsernameFromHeader(), id);
        return ResponseEntity.ok("ok");
    }
    @GetMapping("/get")
    public ResponseEntity<List<ResAnnouncementDto>> get(){
        return ResponseEntity.ok(favouriteService.get(JwtUtils.getUsernameFromHeader()));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> check(@RequestParam Integer id){
        return ResponseEntity.ok(favouriteService.check(id, JwtUtils.getUsernameFromHeader()));
    }
}
