package com.ibm.demo.item;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
    /*@Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/api/item/items")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST")
                .allowedHeaders("header1", "header2", "header3")
                .exposedHeaders("header1", "header2")
                .allowCredentials(true).maxAge(3600);

        // Add more mappings...
    }*/
}


