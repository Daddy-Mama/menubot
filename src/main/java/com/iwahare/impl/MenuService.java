package com.iwahare.impl;

import com.iwahare.dto.Extra;
import com.iwahare.dto.Menu;
import com.iwahare.dto.Product;
import com.iwahare.message.MessageTransportDto;
import com.iwahare.dto.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
    @Autowired
    private IKeyboardService keyboardService;

    public MenuService() {
    }

    public MessageTransportDto operateCallback(List<String> callbackData, User user) {
        switch (callbackData.size()) {
            // GET /category or /back
            case 1: {
                return operateCategory(callbackData.get(0), user.getId());
            }
            case 2: {
                if (callbackData.contains(BACK_TEXT.getValue())) {
                    return operateBackCommand(callbackData.get(1), user.getId());
                }
                return operateProduct(callbackData.get(0), callbackData.get(1), user.getId());
            }
            case 3: {
                return operateExtra(callbackData.get(0), callbackData.get(1), user.getId(), callbackData.get(2));
            }
        }
        return null;
    }

    public MessageTransportDto operateCategory(String categoryName, Integer userId) {
//        if (categoryName.equals(BACK_TEXT.getValue())) {
//            return operateBackToMainMenuCommand();
//        }
        if (categoryName.equals(MENU_TEXT.getValue())) {
            return buildMainMenu(userId);
        }
        Receipt receipt = dataBaseService.getReceiptByUser(userId);
        Menu category = menu.getCategories().stream()
                .filter(x -> x.getName().equals(categoryName))
                .findFirst().get();
        MessageTransportDto messageTransportDto = buildCategory(category);
        messageTransportDto.setReceipt(receipt);
        return messageTransportDto;
    }

//    private MessageTransportDto operateBackToMainMenuCommand() {
//        return buildMainMenu();
//    }

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
    public MessageTransportDto operateExtra(String categoryName, String productName, Integer userId, String extraName) {
        Product productSchema = new Product(menu.getCategories().stream()
                .filter(x -> x.getName().equals(categoryName))
                .map(Menu::getProducts)
                .flatMap(List::stream)
                .filter(prod -> prod.getName().equals(productName))
                .findFirst().get());
        Menu category = menu.getCategories().stream()
                .filter(x -> x.getName().equals(categoryName))
                .findFirst().get();
        MessageTransportDto messageTransportDto = buildProductMenu(category, productSchema);

        Product product = dataBaseService.getLastProductInReceipt(userId);
        Extra extra = category.getProducts().stream()
                .filter(product1 -> productSchema.getName().equals(productName))
                .map(product1 -> productSchema.getExtras())
                .filter(extras -> extras != null)
                .flatMap(extras -> extras.stream())
                .filter(extra1 -> extra1.getName().equals(extraName))
                .findFirst().orElse(null);

        if (extra != null) {
            product.addExtra(extra);
        }
        Receipt receipt = dataBaseService.replaceLastProduct(userId, product);

        messageTransportDto.setReceipt(receipt);

        return messageTransportDto;
    }

    public MessageTransportDto operateBackCommand(String categoryName, Integer userId) {
        dataBaseService.deleteLastProduct(userId);
        return operateCategory(categoryName, userId);

    }

    public MessageTransportDto buildMainMenu(Integer userId) {
        MessageTransportDto messageTransportDto = new MessageTransportDto();
        List<String> buttonNames = new ArrayList<>();
        List<String> buttonCallback = new ArrayList<>();
        Receipt receipt = dataBaseService.getReceiptByUser(userId);
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
        messageTransportDto.setReceipt(receipt);
        messageTransportDto.setInlineKeyboardMarkup(keyboardService.buildInlineKeyboard(buttonNames, buttonCallback));
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
            buttonCallback.add(MENU_TEXT.getValue());
        }
        messageTransportDto.setInlineKeyboardMarkup(keyboardService.buildInlineKeyboard(buttonNames, buttonCallback));
        messageTransportDto.setDesripion(category.getDescription());
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
        buttonNames.add(OK_TEXT.getValue());
        buttonNames.add(BACK_TEXT.getValue());

        buttonCallback.add("/" + category.getName());
        buttonCallback.add("/" + BACK_TEXT.getValue() + "/" + category.getName());

        messageTransportDto.setInlineKeyboardMarkup(keyboardService.buildInlineKeyboard(buttonNames, buttonCallback));
        messageTransportDto.setDesripion(product.getExtraDescription());
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

//    private InlineKeyboardMarkup buildKeyboard(List<String> keyboardIcons, List<String> keyboardCallback) {
//        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        assert keyboardCallback.size() == keyboardIcons.size();
//        Iterator<String> iteratorIcon = keyboardIcons.iterator();
//        Iterator<String> iteratorCallback = keyboardCallback.iterator();
//        while (iteratorIcon.hasNext()) {
//            String buttonName = iteratorIcon.next();
//            InlineKeyboardButton button = new InlineKeyboardButton(buttonName);
//            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
//
//            button.setCallbackData(iteratorCallback.next());
//            keyboardButtonsRow.add(button);
//
//            if (buttonName.equals(OK_TEXT.getValue())) {
//                buttonName = iteratorIcon.next();
//                InlineKeyboardButton backButton = new InlineKeyboardButton(buttonName);
//                backButton.setCallbackData(iteratorCallback.next());
//                keyboardButtonsRow.add(backButton);
//                buttons.add(keyboardButtonsRow);
//                break;
//            }
//
//            buttons.add(keyboardButtonsRow);
//
//
//        }
//        inlineKeyboardMarkup.setKeyboard(buttons);
//        return inlineKeyboardMarkup;
//    }
}