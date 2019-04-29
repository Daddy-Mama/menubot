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

//    @Autowired
//    public Menu() {
//        for (int i = 1; i < 5; i++) {
//            this.products.add(new Product("product_" + i, i));
//        }
////        this.sendMessage = new SendMessage();
////build text part
//        this.header = MENU_TITLE_TEXT.getValue() + "\n\n" + MENU_ADDITIONAL_INFO_TEXT.getValue();
////        sendMessage.setText(text);
////build inline keyboard
//        List<List<InlineKeyboardButton>> buttons =
//                products.stream()
//                        .map(x -> new InlineKeyboardButton(x.getName()))
//                        .map(x -> {
//                            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
//                            keyboardButtonsRow.add(x);
//                            return keyboardButtonsRow;
//                        })
//                        .collect(Collectors.toList());
//
//        this.inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        inlineKeyboardMarkup.setKeyboard(buttons);
//
////        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
//    }

//    public InlineKeyboardMarkup getInlineKeyboardMarkup() {
//        return inlineKeyboardMarkup;
//    }
//
//    public String getHeader() {
//        return header;
//    }

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
