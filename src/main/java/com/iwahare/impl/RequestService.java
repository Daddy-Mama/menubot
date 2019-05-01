package com.iwahare.impl;

import com.iwahare.dto.Menu;
import com.iwahare.dto.Product;
import com.iwahare.message.MessageTransportDto;
import com.iwahare.receipt.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;

import static com.iwahare.enums.CommandsEnum.*;
import static com.iwahare.enums.ReservedWordsEnum.CUSTOMER_MAIN_MENU;


@Service
public class RequestService implements IRequestService {
    private final List<Integer> adminChatList = Arrays.asList();

    @Autowired
    private Menu menu;
    @Autowired
    private IKeyboardService keyboardService;

    @Override
    public MessageTransportDto operatePayment(Update update) {
        return null;
    }

    @Override
    public MessageTransportDto operateCallbackQuery(Update update) {
        List<String> callbackData = Arrays.asList(update.getCallbackQuery().getData().split("/")).stream()
                .filter(x -> !x.equals("/"))
                .filter(x -> !x.isEmpty())
                .collect(Collectors.toList());
        assert callbackData != null && callbackData.size() > 0;
        Menu category = null;
        Receipt receipt = null;
        //GET receipt from callback
        if (callbackData.stream().filter(x -> x.equals(RECEIPT_TEXT.getValue().substring(1)))
                .findFirst().isPresent()) {
            receipt = Receipt.fromJson(callbackData.get(2));
        }
        //operate category from callback
        if (callbackData.stream().filter(x -> x.equals(CATEGORY_TEXT.getValue().substring(1)))
                .findFirst().isPresent()) {
            category = menu.getCategories().stream()
                    .filter(x -> x.getName().equals(callbackData.get(1)))
                    .findFirst().get();
        }
        //operate product from command
        if (callbackData.stream().filter(x -> x.equals(PRODUCT_TEXT.getValue().substring(1)))
                .findFirst().isPresent()) {
            Product product = menu.getCategories().stream()
                    .map(Menu::getProducts)
                    .flatMap(List::stream)
                    .filter(prod -> prod.getName().equals(callbackData.get(1)))
                    .findFirst().get();
            category = menu.getCategories().stream()
                    .filter(x -> x.getProducts().stream().filter(prod -> prod.getName().equals(callbackData.get(1))).findAny().isPresent())
                    .findFirst().get();

           receipt= buildReceipt(receipt, product);
        }
        //Operate /back command
        if (callbackData.stream().filter(x -> x.equals(BACK_TEXT.getValue().substring(1)))
                .findFirst().isPresent()) {
            category = menu;
        }


        return buildMenu(category, receipt);
    }

    private Receipt buildReceipt(Receipt receipt, Product product) {
        if (receipt == null) {
            receipt = new Receipt();
        }
        receipt.addOrder(product);
        return receipt;
    }

    @Override
    public MessageTransportDto operateMessage(Update update) {
        if (adminChatList.contains(update.getMessage().getChatId())) {
            return operateAdminMessage(update);
        } else {
            return operateCustomerMessage(update);


        }
//        return null;
    }

    private MessageTransportDto operateAdminMessage(Update update) {
        return null;
    }

    private MessageTransportDto operateCustomerMessage(Update update) {
        String message = update.getMessage().getText();
        if (message.equals(START_TEXT.getValue()) || message.equals(MENU_TEXT.getValue())) {
            return buildMenu(menu, null);
        } else {

            return buildMenu(null, null);
        }
    }

    private MessageTransportDto buildMenu(Menu menu, Receipt receipt) {
        MessageTransportDto messageTransportDto = new MessageTransportDto();
        //build Inline keyboard
        List<String> keyboardIcons = new ArrayList<>();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        if (menu != null) {
            if (!menu.getCategories().isEmpty()) {
                keyboardIcons.addAll(menu.getCategories().stream()
                        .map(Menu::getName)
                        .filter(x -> x != null)
                        .map(x -> CATEGORY_TEXT.getValue() + "/" + x)
                        .collect(Collectors.toList()));

            }
            if (!menu.getProducts().isEmpty()) {
                keyboardIcons.addAll(menu.getProducts().stream()
                        .map(Product::getName)
                        .filter(x -> x != null)
                        .map(x -> PRODUCT_TEXT.getValue() + "/" + x)
                        .collect(Collectors.toList()));
                keyboardIcons.add(BACK_TEXT.getValue());
            }
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

            for (String keyboardIcon : keyboardIcons) {
                InlineKeyboardButton button = new InlineKeyboardButton(keyboardIcon);
                if (receipt != null) {
                    button.setCallbackData(keyboardIcon + RECEIPT_TEXT.getValue() + "/" + Receipt.toJson(receipt));
                } else {
                    button.setCallbackData(keyboardIcon);
                }
                List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
                keyboardButtonsRow.add(button);
                buttons.add(keyboardButtonsRow);
            }
            inlineKeyboardMarkup.setKeyboard(buttons);

            messageTransportDto.setInlineKeyboardMarkup(inlineKeyboardMarkup);
            messageTransportDto.setDesripion(menu.getDescription());
            messageTransportDto.setPhotoId(menu.getPhotoId());
        } else {
            messageTransportDto.setDesripion(COMMAND_NOT_RECOGNIZED_ERROR.getValue());
            if (receipt == null) {
                messageTransportDto.setKeyboardMarkup(keyboardService.getKeyboard(CUSTOMER_MAIN_MENU.getValue()));
            }
        }


        //build Menu keyboard
        messageTransportDto.setReceipt(receipt);

        return messageTransportDto;
    }
}
