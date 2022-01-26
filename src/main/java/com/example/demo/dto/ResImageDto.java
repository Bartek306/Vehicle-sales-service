package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ResImageDto {
    Integer id;
    byte[] bytes;

}
