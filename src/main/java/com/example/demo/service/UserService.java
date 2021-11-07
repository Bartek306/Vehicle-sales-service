package com.example.demo.service;

import com.example.demo.model.Token;
import com.example.demo.model.UserModel;
import com.example.demo.repository.TokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.Generator;
import com.example.demo.utils.Validator;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public UserModel registerUser(String login, String password, String email, LocalDateTime createdAt){
        UserModel userModel = new UserModel();
        userModel.setLogin(login);
        userModel.setPassword(password);
        userModel.setEmail(email);
        userModel.setCreatedAt(createdAt);
        userModel.setActive(false);
        return userRepository.save(userModel);
    }

    public UserModel authenticate(String login, String password){
        return userRepository.findByLoginAndPassword(login, password).orElse(null);
    }

    public void generateAndSendToken(UserModel registeredUser) {
        String stringToken = Generator.generateToken();
        Token token = new Token(stringToken, registeredUser);
        tokenRepository.save(token);
        // send
    }

    public BindingResult validateRegistration(UserModel userModel, BindingResult bindingResult, String confirmation){
        if(! userModel.getPassword().equals(confirmation)){
            bindingResult.rejectValue("password", "messageCode", "Hasla sie nie zgadzaja");
        }
        /*
        if(!Validator.validatePassword(userModel.getPassword())){
            bindingResult.rejectValue("passsword", "messageCode", "Haslo jest zbyt slabe");
        }
         */
        if(userRepository.findByEmail(userModel.getEmail()).isPresent()){
            bindingResult.rejectValue("email", "messageCode", "Jest juz uzytkownik o takim emailu");
        }
        return bindingResult;
    }

    public BindingResult validateLogin(UserModel userModel, BindingResult bindingResult){

        return bindingResult;
    }
}
