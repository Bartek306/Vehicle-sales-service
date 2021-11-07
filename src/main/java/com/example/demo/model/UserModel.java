package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer Id;
    @Size(min=3, max=16)
    @NotBlank(message = "Pole nie moze byc puste")
    String login;
    @Size(min=2, max=20)
    @NotBlank(message = "Pole nie moze byc puste")
    String password;
    String email;
    LocalDateTime createdAt;
    Boolean active;

}
