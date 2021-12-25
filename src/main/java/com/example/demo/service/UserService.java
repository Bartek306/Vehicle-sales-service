package com.example.demo.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.dto.LoginDto;
import com.example.demo.dto.RegisterDto;
import com.example.demo.dto.UpdateUserDto;
import com.example.demo.exception.UserException;
import com.example.demo.model.City;
import com.example.demo.model.UserModel;
import com.example.demo.repository.CityRepository;
import com.example.demo.repository.UserRepository;

import com.example.demo.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
@Service()
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;


    @Transactional
    public UserModel registerUser(RegisterDto registerDto){
        UserModel userModel = new UserModel();
        if(userRepository.findByEmail(registerDto.getEmail()).orElse(null) != null){
            throw new UserException("There is user with these email");
        }

        if(userRepository.findByLogin(registerDto.getLogin()).orElse(null) != null){
            throw new UserException("There is user with these login");
        }
        userModel.setEmail(registerDto.getEmail());
        userModel.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userModel.setLogin(registerDto.getLogin());
        userModel.setCreatedAt(LocalDateTime.now().toString());
        userModel.setActive(true);
        userRepository.save(userModel);
        return userModel;
    }
    @Transactional
    public String login(LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getLogin(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserModel user = (UserModel) authentication.getPrincipal();
        System.out.println(user);
        if(user == null){
            throw new UserException("Email or password is invalid");
        }
        return jwt;
    }

    @Transactional
    public UserModel update(UpdateUserDto updateUserDto, String owner) {
        UserModel userModel = userRepository.findByLogin(owner).get();
        userModel.setPhoneNumber(updateUserDto.getPhone());
        userModel.setCity(cityRepository.findByName(updateUserDto.getCity()).get());
        return userModel;
    }

}
