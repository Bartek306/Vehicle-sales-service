package com.example.demo.controller;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.RegisterDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.UpdateUserDto;
import com.example.demo.model.UserModel;
import com.example.demo.security.WebSecurityConfig;
import com.example.demo.service.UserService;
import com.example.demo.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(userService.login(loginDto));
    }

    @PostMapping("/update")
    public ResponseEntity<UserModel> update(@RequestBody UpdateUserDto updateUserDto) {
        return ResponseEntity.ok(userService.update(updateUserDto, JwtUtils.getUsernameFromHeader()));

    }
}

