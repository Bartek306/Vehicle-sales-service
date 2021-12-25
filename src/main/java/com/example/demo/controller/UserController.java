package com.example.demo.controller;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.LoginResponseDto;
import com.example.demo.dto.RegisterDto;
import com.example.demo.dto.UpdateUserDto;
import com.example.demo.model.UserModel;
import com.example.demo.security.WebSecurityConfig;
import com.example.demo.service.UserService;
import com.example.demo.utils.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Import({ WebSecurityConfig.class })
@RestController
@CrossOrigin
@RequestMapping("/auth")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserModel> register(@RequestBody RegisterDto registerDto) {
        return ResponseEntity.ok(userService.registerUser(registerDto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(userService.login(loginDto));
    }

    @PostMapping("/update")
    public ResponseEntity<UserModel> update(@RequestBody UpdateUserDto updateUserDto) {
        return ResponseEntity.ok(userService.update(updateUserDto, JwtUtils.getUsernameFromHeader()));

    }
}

