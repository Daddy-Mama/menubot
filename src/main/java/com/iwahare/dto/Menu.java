package com.iwahare.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Menu {
    private List<Product> products = new ArrayList<>();
    private List<Menu> categories = new ArrayList<>();
    private List<Extra> extras = new ArrayList<>();
    private String photoId;
    private String description;
    private String name;
    private String preNameEmoji;
    private String postNameEmoji;

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

    public Menu(Product product) {
        this.description = product.getExtraDescription();
        this.extras = product.getExtras();
        this.products.add(product);
    }

    public String getButtonName() {
        String result = new String(name);
        if (!preNameEmoji.isEmpty()) {
            result = preNameEmoji + result;
        }
        if (!postNameEmoji.isEmpty()) {
            result = result + postNameEmoji;
        }
        return result;

    }

    public String getButtonCallback() {
        return "/" + name;
    }

    public String getPreNameEmoji() {
        return preNameEmoji;
    }

    public String getPostNameEmoji() {
        return postNameEmoji;
    }

    public List<Extra> getExtras() {
        return extras;
    }

    public void setExtras(List<Extra> extras) {
        this.extras = extras;
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
