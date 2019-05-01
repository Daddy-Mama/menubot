package com.iwahare.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.iwahare.enums.ReservedWordsEnum.MENU_ADDITIONAL_INFO_TEXT;
import static com.iwahare.enums.ReservedWordsEnum.MENU_TITLE_TEXT;

public class Menu {
    private  List<Product> products = new ArrayList<>();
    private List<Menu> categories = new ArrayList<>();
    private String photoId;
//    private InlineKeyboardMarkup inlineKeyboardMarkup;
    private String description;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }


    public Menu() {
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Menu> getCategories() {
        return categories;
    }

    public void setCategories(List<Menu> categories) {
        this.categories = categories;
    }

 }
