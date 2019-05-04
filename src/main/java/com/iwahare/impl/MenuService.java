package com.iwahare.impl;

import com.iwahare.dto.Extra;
import com.iwahare.dto.Menu;
import com.iwahare.dto.MenuBuilder;
import com.iwahare.dto.Product;
import com.iwahare.message.MessageTransportDto;
import com.iwahare.receipt.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sun.applet.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.iwahare.enums.CommandsEnum.*;

/**
 * Created by Артем on 03.05.2019.
 */
@Service
public class MenuService implements IMenuService {
    @Autowired
    private Menu menu;
    @Autowired
    private IDataBaseService dataBaseService;

    public MenuService() {
    }

    public MessageTransportDto operateCategory(String categoryName, Integer userId) {
        if (categoryName.equals(BACK_TEXT.getValue())) {
            return operateBackToMainMenuCommand();
        }
        Receipt receipt = dataBaseService.getReceiptByUser(userId);
        Menu category = menu.getCategories().stream()
                .filter(x -> x.getName().equals(categoryName))
                .findFirst().get();
        MessageTransportDto messageTransportDto = buildCategory(category);
        messageTransportDto.setReceipt(receipt);
        return messageTransportDto;
    }

    private MessageTransportDto operateBackToMainMenuCommand() {
        return buildMainMenu();
    }

    @Override
    public MessageTransportDto operateProduct(String categoryName, String productName, Integer userId) {
        Product product = new Product(menu.getCategories().stream()
                .filter(x -> x.getName().equals(categoryName))
                .map(Menu::getProducts)
                .flatMap(List::stream)
                .filter(prod -> prod.getName().equals(productName))
                .findFirst().get());
        Menu category;
        MessageTransportDto messageTransportDto;
        category = menu.getCategories().stream()
                .filter(x -> x.getName().equals(categoryName))
                .findFirst().get();
        if (product.getExtras() != null) {


            messageTransportDto = buildProductMenu(category, product);
        } else {

            messageTransportDto = buildCategory(category);
        }

        product.setExtras(null);
        Receipt receipt = dataBaseService.buildReceipt(userId, product);
        messageTransportDto.setReceipt(receipt);

        return messageTransportDto;
    }

    @Override
    public MessageTransportDto operateExtra(String categoryName, String productName, Integer userId, List<String> extraName) {
        Product product = new Product(menu.getCategories().stream()
                .filter(x -> x.getName().equals(categoryName))
                .map(Menu::getProducts)
                .flatMap(List::stream)
                .filter(prod -> prod.getName().equals(productName))
                .findFirst().get());
        Menu category = menu.getCategories().stream()
                .filter(x -> x.getName().equals(categoryName))
                .findFirst().get();
        MessageTransportDto messageTransportDto = buildProductMenu(category, product);


        List<Extra> extras = product.getExtras().stream()
                .filter(x -> extraName.contains(x.getName()))
                .collect(Collectors.toList());

        product.setExtras(extras);
        Receipt receipt = dataBaseService.replaceLastProduct(userId, product);

        messageTransportDto.setReceipt(receipt);

        return messageTransportDto;
    }

    public MessageTransportDto buildMainMenu() {
        MessageTransportDto messageTransportDto = new MessageTransportDto();
        List<String> buttonNames = new ArrayList<>();
        List<String> buttonCallback = new ArrayList<>();
        if (!menu.getCategories().isEmpty()) {
            buttonNames.addAll(menu.getCategories().stream()
                    .map(Menu::getName)
                    .filter(x -> x != null)
                    .collect(Collectors.toList()));
            buttonCallback.addAll(buttonNames.stream()
                    .map(x -> "/" + x)
                    .collect(Collectors.toList()));
        }
//        //operate product
//        if (!menu.getProducts().isEmpty()) {
//            buttonNames.addAll(menu.getProducts().stream()
//                    .map(Product::getName)
//                    .filter(x -> x != null)
//                    .collect(Collectors.toList()));
//            buttonCallback.addAll(buttonNames.stream()
//                    .map(x -> "/" + x)
//                    .collect(Collectors.toList()));
//            buttonNames.add(BACK_TEXT.getValue());
//        }
        messageTransportDto.setInlineKeyboardMarkup(buildKeyboard(buttonNames, buttonCallback));
        messageTransportDto.setDesripion(menu.getDescription());
        messageTransportDto.setPhotoId(menu.getPhotoId());
        return messageTransportDto;
    }

    public MessageTransportDto buildCategory(Menu category) {
        MessageTransportDto messageTransportDto = new MessageTransportDto();
        List<String> buttonNames = new ArrayList<>();
        List<String> buttonCallback = new ArrayList<>();
        if (!category.getProducts().isEmpty()) {
            buttonNames.addAll(category.getProducts().stream()
                    .map(Product::getName)
                    .filter(x -> x != null)
                    .collect(Collectors.toList()));

            buttonCallback.addAll(buttonNames.stream()
                    .map(x -> category.getName() + "/" + x)
                    .collect(Collectors.toList()));
            buttonNames.add(BACK_TEXT.getValue());
            buttonCallback.add(BACK_CALLBACK_TEXT.getValue());
        }
        messageTransportDto.setInlineKeyboardMarkup(buildKeyboard(buttonNames, buttonCallback));
        messageTransportDto.setDesripion(menu.getDescription());
        messageTransportDto.setPhotoId(menu.getPhotoId());
        return messageTransportDto;
    }

    public MessageTransportDto buildProductMenu(Menu category, Product product) {
        MessageTransportDto messageTransportDto = new MessageTransportDto();
        List<String> buttonNames = new ArrayList<>();
        List<String> buttonCallback = new ArrayList<>();

        buttonNames.addAll(product.getExtras().stream()
                .map(Extra::getName)
                .filter(x -> x != null)
                .collect(Collectors.toList()));
        buttonCallback.addAll(buttonNames.stream()
                .map(x -> category.getName() + "/" + product.getName() + "/" + x)
                .collect(Collectors.toList()));
        buttonNames.add(BACK_TEXT.getValue());

        buttonCallback.add(BACK_CALLBACK_TEXT.getValue());

        messageTransportDto.setInlineKeyboardMarkup(buildKeyboard(buttonNames, buttonCallback));
        messageTransportDto.setDesripion(menu.getDescription());
        messageTransportDto.setPhotoId(menu.getPhotoId());
        return messageTransportDto;
    }

//    public MessageTransportDto buildMenu(MenuBuilder builder) {
//        MessageTransportDto messageTransportDto = new MessageTransportDto();
//        List<String> buttonNames = new ArrayList<>();
//        List<String> buttonCallback = new ArrayList<>();
//        if (builder.getExtras() != null) {
//            buttonNames.addAll(builder.getExtras().stream()
//                    .map(Extra::getName)
//                    .filter(x -> x != null)
//                    .collect(Collectors.toList()));
//            buttonNames.add(OK_TEXT.getValue());
//            buttonNames.add(BACK_TEXT.getValue());
//            buttonCallback.addAll(builder.getExtras().stream()
//                    .map(Extra::getName)
//                    .map(x -> builder.getCategory().getName() + "/" + builder.getProduct().getName() + "/" + builder.getProduct().getExtraUrl() + "/" + x)
//                    .filter(x -> x != null)
//                    .collect(Collectors.toList()));
//            buttonCallback.add(OK_CALLBACK_TEXT.getValue());
//            buttonCallback.add(BACK_CALLBACK_TEXT.getValue());
//
//        } else if (builder.getCategory() != null) {
//            if (!builder.getCategory().getCategories().isEmpty()) {
//                buttonNames.addAll(menu.getCategories().stream()
//                        .map(Menu::getName)
//                        .filter(x -> x != null)
//                        .collect(Collectors.toList()));
//                buttonCallback.addAll()
//            }
//            //operate product
//            if (!menu.getProducts().isEmpty()) {
//                buttonNames.addAll(menu.getProducts().stream()
//                        .map(Product::getName)
//                        .filter(x -> x != null)
//                        .collect(Collectors.toList()));
//                buttonNames.add(BACK_TEXT.getValue());
//            }
//        }
//
//
//        if (builder.)
//            //build Inline keyboard
//            List<String> keyboardIcons = new ArrayList<>();
//        String buttonCallbackData = "";
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        if (menu != null) {
//            if (!menu.getCategories().isEmpty()) {
//                keyboardIcons.addAll(menu.getCategories().stream()
//                        .map(Menu::getName)
//                        .filter(x -> x != null)
//                        .collect(Collectors.toList()));
//            }
//            //operate product
//            if (!menu.getProducts().isEmpty()) {
//                keyboardIcons.addAll(menu.getProducts().stream()
//                        .map(Product::getName)
//                        .filter(x -> x != null)
//                        .collect(Collectors.toList()));
////                buttonCallbackData = PRODUCT_TEXT.getValue();
//                keyboardIcons.add(BACK_TEXT.getValue());
//            }
//            //operate extras for product
//            if (!menu.getExtras().isEmpty()) {
//                keyboardIcons.addAll(menu.getExtras().stream()
//                        .map(Extra::getName)
//                        .filter(x -> x != null)
//                        .collect(Collectors.toList()));
//                keyboardIcons.add(OK_TEXT.getValue());
//                keyboardIcons.add(BACK_TEXT.getValue());
//            }
//
//            messageTransportDto.setInlineKeyboardMarkup(inlineKeyboardMarkup);
//            messageTransportDto.setDesripion(menu.getDescription());
//            messageTransportDto.setPhotoId(menu.getPhotoId());
//        } else {
//            messageTransportDto.setDesripion(COMMAND_NOT_RECOGNIZED_ERROR.getValue());
//
//        }
//
//        return messageTransportDto;
//    }

    private InlineKeyboardMarkup buildKeyboard(List<String> keyboardIcons, List<String> keyboardCallback) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        assert keyboardCallback.size() == keyboardIcons.size();
        for (int i = 0; i < keyboardCallback.size(); i++) {

            InlineKeyboardButton button = new InlineKeyboardButton(keyboardIcons.get(i));
            button.setCallbackData(keyboardCallback.get(i));
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
            keyboardButtonsRow.add(button);
            buttons.add(keyboardButtonsRow);
        }
        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }
}