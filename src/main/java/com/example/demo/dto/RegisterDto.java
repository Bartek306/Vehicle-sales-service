package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
@Getter
@Setter
public class RegisterDto {
    @Size(min=3, max=16)
    @NotBlank(message = "Pole nie moze byc puste")
    String login;
    @Size(min=2, max=20)
    @NotBlank(message = "Pole nie moze byc puste")
    String password;
    @Email
    String email;
}
