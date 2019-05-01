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
import org.telegram.telegrambots.ApiContextInitializer;

import static com.iwahare.enums.CommandsEnum.CATEGORY_TEXT;
import static com.iwahare.enums.CommandsEnum.PRODUCT_TEXT;

@SpringBootApplication
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
        File json = new File("C:\\Users\\Артем.Артем-ПК\\Documents\\Java\\mbot\\menubot\\src\\main\\resources\\menu.json");
        Menu menu = mapper.readValue(json, Menu.class);
//        menu.getCategories().forEach(x -> x.setName(CATEGORY_TEXT.getValue() + x.getName()));
//        menu.getCategories().forEach(x -> x.getProducts().forEach(product -> product.setName(PRODUCT_TEXT.getValue() + product.getName())));
//        menu.getProducts().forEach(product -> product.setName(PRODUCT_TEXT.getValue() + product.getName()));
        return menu;
    }
}