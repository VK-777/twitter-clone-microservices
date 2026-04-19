package com.group1.search.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

   @Bean
   public ModelMapper modelMapper() {
       return new ModelMapper();
   }

}

