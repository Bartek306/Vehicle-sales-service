package com.example.demo.datagenerator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RunOnStart implements ApplicationRunner {

    private final GenerateData generateData;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        generateData.generateUser();
        generateData.generateCityAndVoivodeship();
        generateData.generateAnnouncement();
    }
}
