package com.example.demo.controller;

import com.example.demo.model.UserModel;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("/register")
    public String getRegisterPage(Model model){
        model.addAttribute("registerRequest", new UserModel());
        return "register_page";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute(value="registerRequest") UserModel  userModel, BindingResult bindingResult, @RequestParam("confirm") String confirmation) {
        System.out.println(userModel);
        bindingResult = userService.validateRegistration(userModel, bindingResult, confirmation);
        if (bindingResult.hasErrors()) {
            return "register_page";
        } else {
            UserModel registeredUser = userService.registerUser(userModel.getLogin(), userModel.getPassword(), userModel.getEmail(), LocalDateTime.now());
            userService.generateAndSendToken(registeredUser);
            return registeredUser == null ? "error_page" : "redirect:/login";
        }
    }

    @GetMapping("/login")
    public String getLoginPage(Model model){
        model.addAttribute("loginRequest", new UserModel());
        return "login_page";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserModel userModel, Model model) {
        System.out.println("login request " + userModel);
        UserModel authenticateModel = userService.authenticate(userModel.getLogin(), userModel.getPassword());
        if (authenticateModel == null) {
            return "error_page";
        } else {
            model.addAttribute("userLogin", authenticateModel.getLogin());
            return "personal_page";
        }
    }
}
