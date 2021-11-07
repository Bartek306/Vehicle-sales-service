package com.example.demo.controller;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.LoginResponseDto;
import com.example.demo.dto.RegisterDto;
import com.example.demo.model.UserModel;
import com.example.demo.security.WebSecurityConfig;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Import({ WebSecurityConfig.class })
@RestController
@CrossOrigin
@RequestMapping("/auth")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserModel> register(@RequestBody RegisterDto registerDto){
       return ResponseEntity.ok(userService.registerUser(registerDto));
    }

    @PostMapping("/login")
    public  ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto){
        return ResponseEntity.ok(userService.login(loginDto));

    }

}
