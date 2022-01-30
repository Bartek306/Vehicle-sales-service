package com.example.demo;

import com.example.demo.repository.ImageRepository;
import com.example.demo.utils.MyUtils;
import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {
    @Autowired private ImageRepository imageRepository;
    @Bean
    public ModelMapper modelMapper() {
        var modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD)
                .setSkipNullEnabled(true)
                .setPropertyCondition(context -> !(context.getSource() instanceof PersistentCollection));
        return modelMapper;
    }

    @Bean
    public MyUtils myUtils(){ return new MyUtils(imageRepository, modelMapper());}
}
