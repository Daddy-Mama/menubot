package com.iwahare;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwahare.dto.Menu;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@EnableScheduling
public class MbotApplication {
    private static Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(MbotApplication.class, args);
        logger.info("=======================APPLICATION STARTED =====================");
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public static Menu buildMenuSingleton() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File json = new File("/home/intellica/Documents/Projects/menubot/src/main/resources/menu.json");
        Menu menu = mapper.readValue(json, Menu.class);
        return menu;
    }
}
