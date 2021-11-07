package com.example.demo.service;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.LoginResponseDto;
import com.example.demo.dto.RegisterDto;
import com.example.demo.exception.UserException;
import com.example.demo.model.UserModel;
import com.example.demo.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    @Autowired
    private final PasswordEncoder passwordEncoder;

    private final String jwtSecret = "secretJWt";

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
        userModel.setActive(false);
        userRepository.save(userModel);
        return userModel;
    }
    @Transactional
    public LoginResponseDto login(LoginDto loginDto){
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getLogin(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var user = (UserModel) authentication.getPrincipal();
        System.out.println(user);
        if(user == null){
            throw new UserException("Email or password is invalid");
        }
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setId(user.getId());
        loginResponseDto.setToken(generateToken(user));
        return loginResponseDto;
    }

    private String generateToken(UserModel userModel){
        return Jwts.builder().setSubject(userModel.getEmail())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, this.jwtSecret)
                .compact();
    }


}
