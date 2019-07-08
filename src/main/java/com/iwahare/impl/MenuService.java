package com.iwahare.impl;

import com.iwahare.dto.Extra;
import com.iwahare.dto.Menu;
import com.iwahare.dto.Product;
import com.iwahare.message.MessageTransportDto;
import com.iwahare.dto.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.iwahare.enums.CommandsEnum.*;
import static com.iwahare.enums.ReservedWordsEnum.*;

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
    @Autowired
    private IReceiptService receiptService;

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

        if (categoryName.equals(BACK_TO_MENU_CALLBACK.getValue())) {
            return buildMainMenu(userId);
        }
        Receipt receipt = dataBaseService.getReceiptByUser(userId);
        Menu category = menu.getCategories().stream()
                .filter(x -> x.getName().equals(categoryName))
                .findFirst().get();
        MessageTransportDto messageTransportDto = buildCategory(category);
        messageTransportDto.setReceiptText(receiptService.toCustomerForm(receipt));
        return messageTransportDto;
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

        Product productSchema = new Product(product);

        product.setExtras(null);
        product.setPreNameEmoji(category.getPreNameEmoji());
        product.setCategoryName(categoryName);
        Receipt receipt = dataBaseService.buildReceipt(userId, product);

        if (productSchema.getExtras() != null) {
            messageTransportDto = buildProductMenu(userId, category, productSchema);
        } else {
            messageTransportDto = buildCategory(category);
        }


        messageTransportDto.setReceiptText(receiptService.toCustomerForm(receipt));

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
        MessageTransportDto messageTransportDto = buildProductMenu(userId, category, productSchema);

        messageTransportDto.setReceiptText(receiptService.toCustomerForm(receipt));

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
                    .map(Menu::getButtonName)
                    .filter(x -> x != null)
                    .collect(Collectors.toList()));
            buttonCallback.addAll(menu.getCategories().stream()
                    .map(Menu::getButtonCallback)
                    .filter(x -> x != null)
                    .collect(Collectors.toList()));
        }
        messageTransportDto.setReceiptText(receiptService.toCustomerForm(receipt));
        if (receipt != null) {
            addOrderMenuButton(buttonNames, buttonCallback);
        }
        messageTransportDto.setInlineKeyboardMarkup(keyboardService.buildInlineKeyboard(buttonNames, buttonCallback));
        messageTransportDto.setDesripion(menu.getDescription());
        messageTransportDto.setPhotoId(menu.getPhotoId());
        return messageTransportDto;
    }

    private void addOrderMenuButton(List<String> buttonNames, List<String> buttonCallback) {
        buttonNames.add(ORDER_MENU_BUTTON.getValue());
        buttonCallback.add(ORDER_MENU_CALLBACK.getValue());
    }

    public MessageTransportDto buildCategory(Menu category) {
        MessageTransportDto messageTransportDto = new MessageTransportDto();
        List<String> buttonNames = new ArrayList<>();
        List<String> buttonCallback = new ArrayList<>();
        if (!category.getProducts().isEmpty()) {
            buttonNames.addAll(category.getProducts().stream()
                    .map(Product::getButtonName)
                    .filter(x -> x != null)
                    .collect(Collectors.toList()));

            buttonCallback.addAll(category.getProducts().stream()
                    .map(x -> category.getButtonCallback() + x.getButtonCallback())
                    .collect(Collectors.toList()));
            buttonNames.add(BACK_TEXT.getValue());
            buttonCallback.add(BACK_TO_MENU_CALLBACK.getValue());
        }
        messageTransportDto.setInlineKeyboardMarkup(keyboardService.buildInlineKeyboard(buttonNames, buttonCallback));
        messageTransportDto.setDesripion(category.getDescription());
        messageTransportDto.setPhotoId(menu.getPhotoId());
        return messageTransportDto;
    }

    public MessageTransportDto buildProductMenu(Integer userId, Menu category, Product product) {
        MessageTransportDto messageTransportDto = new MessageTransportDto();
        List<String> buttonNames = new ArrayList<>();
        List<String> buttonCallback = new ArrayList<>();
        Receipt receipt = dataBaseService.getReceiptByUser(userId);


        buttonNames.addAll(product.getExtras().stream()
                .map(Extra::getButtonName)
                .filter(x -> x != null)
                .collect(Collectors.toList()));

        if (receipt != null && receipt.getOrders() != null) {
            List<Product> orders = receipt.getOrders();
            List<Extra> extras = orders.get(orders.size() - 1).getExtras();
            if (extras != null) {
                extras.stream()
                        .map(extra -> extra.getButtonName())
//                        .filter(name-> buttonNames.contains(name))
                        .forEach(name -> {
                            if (buttonNames.contains(name)) {
                                int i = buttonNames.indexOf(name);
                                buttonNames.set(i, buttonNames.get(i).replace(EMOGI_ADD_EXTRA.getValue(), EMOGI_REMOVE_EXTRA.getValue()));
                            }
                        });
            }
        }
        buttonCallback.addAll(product.getExtras().stream()
                .map(x -> category.getButtonCallback() + "/" + product.getButtonCallback() + "/" + x.getButtonCallback())
                .collect(Collectors.toList()));
        buttonNames.add(OK_TEXT.getValue());
        buttonNames.add(BACK_TEXT.getValue());

        buttonCallback.add(BACK_TO_MENU_CALLBACK.getValue());
        buttonCallback.add("/" + (BACK_TEXT.getValue() + "/" + category.getButtonCallback()));

        messageTransportDto.setInlineKeyboardMarkup(keyboardService.buildInlineKeyboard(buttonNames, buttonCallback));
        messageTransportDto.setDesripion(product.getExtraDescription());
        messageTransportDto.setPhotoId(menu.getPhotoId());
        return messageTransportDto;
    }
}