package com.iwahare.impl;

import com.iwahare.dto.Menu;
import com.iwahare.message.MessageTransportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;

import static com.iwahare.enums.CommandsEnum.*;
import static com.iwahare.enums.ReservedWordsEnum.MY_ORDERS_TEXT;


@Service
public class RequestService implements IRequestService {
    private final List<Integer> adminChatList = Arrays.asList();

    @Autowired
    private Menu menu;
    @Autowired
    private IKeyboardService keyboardService;
    @Autowired
    private IDataBaseService dataBaseService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IOrderService orderService;
    private final List<Long> adminChatIds = Arrays.asList(388466771L);

    @Override
    public MessageTransportDto operatePayment(Update update) {
        MessageTransportDto messageTransportDto = orderService.operatePayment(update);
        messageTransportDto.setChat_id(adminChatIds);
        return messageTransportDto;
    }

    @Override
    public MessageTransportDto operateCallbackQuery(Update update) {
        List<String> callbackData = Arrays.stream(update.getCallbackQuery().getData().split("/"))
                .filter(x -> !x.equals("/"))
                .filter(x -> !x.isEmpty())
                .collect(Collectors.toList());
        assert callbackData != null && callbackData.size() > 0;
        User user = update.getCallbackQuery().getFrom();
        if (callbackData.contains(ORDER_MENU_CALLBACK.getValue())) {
            return orderService.operateCallback(callbackData, user, update);
        } else {
            return menuService.operateCallback(callbackData, user);
        }
    }

    @Override
    public MessageTransportDto operateMessage(Update update) {
        String message = update.getMessage().getText();

        if (message.equals(START_TEXT.getValue())) {
            MessageTransportDto messageTransportDto = menuService.buildMainMenu(update.getMessage().getFrom().getId());
//            messageTransportDto.setDesripion("Добро пожаловать!");
            return messageTransportDto;
        } else {
            MessageTransportDto messageTransportDto = new MessageTransportDto();
            messageTransportDto.setDesripion(COMMAND_NOT_RECOGNIZED_ERROR.getValue());
            return messageTransportDto;
        }

    }

    private MessageTransportDto operateAdminMessage(Update update) {
        return null;
    }

//    private MessageTransportDto operateCustomerMessage(Update update) {
//        String message = update.getMessage().getText();
//
//        if (message.equals(START_TEXT.getValue())) {
//            MessageTransportDto messageTransportDto = new MessageTransportDto();
//            messageTransportDto.setDesripion("Добро пожаловать!");
//            messageTransportDto.setKeyboardMarkup(keyboardService.getKeyboard());
//            return messageTransportDto;
//        }
//        if (message.equals(MY_ORDERS_TEXT.getValue())) {
//            User user = update.getMessage().getFrom();
//            return orderService.buildOrderMenu(user.getId());
//        }
//        if (message.toLowerCase().equals(MENU_TEXT.getValue().toLowerCase())) {
//            User user = update.getMessage().getFrom();
//            return menuService.buildMainMenu(user.getId());
//        } else {
//            MessageTransportDto messageTransportDto = new MessageTransportDto();
//            messageTransportDto.setDesripion(COMMAND_NOT_RECOGNIZED_ERROR.getValue());
//            return messageTransportDto;
//        }
//    }
}

