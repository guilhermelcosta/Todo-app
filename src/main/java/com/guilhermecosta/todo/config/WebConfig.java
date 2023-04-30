package com.guilhermecosta.todo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer { //o WebMvcConfigurer implementa alguns metodos ja prontos

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**"); //Indica que toda requisicao apos o :8080/ sera permitido
    }

}
