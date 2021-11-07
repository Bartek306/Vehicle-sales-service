package com.example.demo.utils;

import java.util.UUID;

public class Generator {

    public static String generateToken(){
        return UUID.randomUUID().toString();
    }

    public static String generatePassword(){
        return null;
    }
}
