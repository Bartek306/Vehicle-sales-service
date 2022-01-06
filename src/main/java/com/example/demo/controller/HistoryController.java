package com.example.demo.controller;

import com.example.demo.dto.ResAnnouncementDto;
import com.example.demo.service.HistoryService;
import com.example.demo.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class HistoryController {
    private final HistoryService historyService;

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestParam Integer id){
        historyService.add(JwtUtils.getUsernameFromHeader(), id);
        return ResponseEntity.ok("ok");
    }
    @GetMapping("/get")
    public ResponseEntity<List<ResAnnouncementDto>> get(){
        return ResponseEntity.ok(historyService.get(JwtUtils.getUsernameFromHeader()));
    }
}
