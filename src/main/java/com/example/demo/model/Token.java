package com.example.demo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer Id;
    @Column(nullable=false)
    private String token;


    @ManyToOne
    @JoinColumn(
            nullable = false,
            name= "user_model_id"
    )
    private UserModel userModel;

    public Token(String token, UserModel userModel){
        this.token = token;
        this.userModel = userModel;
    }
}
