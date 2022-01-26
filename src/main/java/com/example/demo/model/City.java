package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})

public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    private String name;

    @Override
    public String toString(){
        return name;
    }
}
