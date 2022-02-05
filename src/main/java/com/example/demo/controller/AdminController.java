package com.example.demo.controller;

import com.example.demo.dto.ResAnnouncementDto;
import com.example.demo.dto.StatisticDto;
import com.example.demo.dto.UsersInfoDto;
import com.example.demo.security.WebSecurityConfig;
import com.example.demo.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Import({ WebSecurityConfig.class })
@RestController
@CrossOrigin
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private AdminService adminService;

    @GetMapping("get_users_info")
    public ResponseEntity<UsersInfoDto> getUsers(){
        return ResponseEntity.ok(adminService.getUsers());
    }

    @PutMapping("ban_user")
    public ResponseEntity<String> banUser(@RequestParam String login){
        return ResponseEntity.ok(adminService.banUser(login));
    }

    @GetMapping("get_info")
    public ResponseEntity<StatisticDto> getInfo(){
        return ResponseEntity.ok(adminService.getInfo());
    }

    @GetMapping("get_announcement")
    public ResponseEntity<List<ResAnnouncementDto>> getAnnouncement(){
        return ResponseEntity.ok(adminService.getAnnouncement());
    }
}
