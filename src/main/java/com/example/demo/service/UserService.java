package com.example.demo.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.dto.LoginDto;
import com.example.demo.dto.RegisterDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.UpdateUserDto;
import com.example.demo.exception.UserException;
import com.example.demo.model.City;
import com.example.demo.model.Favourite;
import com.example.demo.model.History;
import com.example.demo.model.UserModel;
import com.example.demo.repository.CityRepository;
import com.example.demo.repository.FavouriteRepository;
import com.example.demo.repository.HistoryRepository;
import com.example.demo.repository.UserRepository;

import com.example.demo.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
    private final FavouriteRepository favouriteRepository;
    private final HistoryRepository historyRepository;
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
        History history = new History();
        Favourite favourite = new Favourite();
        userModel.setEmail(registerDto.getEmail());
        userModel.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userModel.setLogin(registerDto.getLogin());
        userModel.setCreatedAt(LocalDateTime.now().toString());
        userModel.setActive(true);
        history.setOwner(userModel);
        userModel.setHistory(history);
        favourite.setOwner(userModel);
        userModel.setFavourite(favourite);
        userRepository.save(userModel);
        favouriteRepository.save(favourite);
        historyRepository.save(history);
        return userModel;
    }
    @Transactional
    public TokenDto login(LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getLogin(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserModel user = (UserModel) authentication.getPrincipal();
        System.out.println(user);
        if(user == null){
            throw new UserException("Email or password is invalid");
        }
        TokenDto tokenDto = new TokenDto();
        tokenDto.setToken(jwt);
        tokenDto.setRole(user.getRole());
        return tokenDto;

    }

    @Transactional
    public String update(String phone, String name, String owner) {
        UserModel userModel = userRepository.findByLogin(owner).get();
        userModel.setPhoneNumber(phone);
        userModel.setCity(cityRepository.findByName(name).get());
        return "OK";
    }

    public String getCity(String login) {
        UserModel userModel = userRepository.findByLogin(login).get();
        if(userModel.getCity() != null){
            return userModel.getCity().getName();
        }
        else {
            return "none";
        }

    }

    public String getUser(String token) {
        return "";
    }

    public String getPhone(String userLogin) {
        UserModel userModel = userRepository.findByLogin(userLogin).get();
        return userModel.getPhoneNumber();
    }
}
