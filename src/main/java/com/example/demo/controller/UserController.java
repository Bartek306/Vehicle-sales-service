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
import org.springframework.security.core.userdetails.User;
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
    public ResponseEntity<String> update(@RequestParam String phone, String city) {
        return ResponseEntity.ok(userService.update(phone, city, JwtUtils.getUsernameFromHeader()));
    }


    @GetMapping("get")
    public ResponseEntity<String> getUserFromToken(@RequestParam String token){
        return ResponseEntity.ok(userService.getUser(token));
    }

    @GetMapping("get_phone")
    public ResponseEntity<String> getPhone(@RequestParam String userLogin){
        return ResponseEntity.ok(userService.getPhone(userLogin));
    }

    @GetMapping("get_role")
    public ResponseEntity<String>getRole(){
        return ResponseEntity.ok(userService.getRole(JwtUtils.getUsernameFromHeader()));
    }

}

