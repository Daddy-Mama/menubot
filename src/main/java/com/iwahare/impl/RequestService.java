package com.iwahare.impl;

import com.iwahare.dto.Extra;
import com.iwahare.dto.Menu;
import com.iwahare.dto.Product;
import com.iwahare.message.MessageTransportDto;
import com.iwahare.receipt.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
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
    @Autowired
    private IDataBaseService dataBaseService;
    @Autowired
    private IMenuService menuService;

    @Override
    public MessageTransportDto operatePayment(Update update) {
        return null;
    }

    @Override
    public MessageTransportDto operateCallbackQuery(Update update) {
        List<String> callbackData = Arrays.stream(update.getCallbackQuery().getData().split("/"))
                .filter(x -> !x.equals("/"))
                .filter(x -> !x.isEmpty())
                .collect(Collectors.toList());
        assert callbackData != null && callbackData.size() > 0;
        User user = update.getCallbackQuery().getFrom();

        switch (callbackData.size()) {
            // GET /category or /back
            case 1: {
                return menuService.operateCategory(callbackData.get(0), user.getId());
            }
            case 2: {
                return menuService.operateProduct(callbackData.get(0), callbackData.get(1), user.getId());
            }

        }
        if (callbackData.size() >= 3) {
            List<String> callbackDataList = new ArrayList<>();
            for (int i = 2; i < callbackData.size(); i++) {
                callbackDataList.add(callbackData.get(i));
            }
            return menuService.operateExtra(callbackData.get(0), callbackData.get(1), user.getId(), callbackDataList);

        }
        return null;
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

        if (message.equals(START_TEXT.getValue())) {
            MessageTransportDto messageTransportDto = new MessageTransportDto();
            messageTransportDto.setDesripion("Добро пожаловать!");
            messageTransportDto.setKeyboardMarkup(keyboardService.getKeyboard());
            return messageTransportDto;
        }
        if (message.equals(MENU_TEXT.getValue())) {
            return menuService.buildMainMenu();
        } else {
            MessageTransportDto messageTransportDto = new MessageTransportDto();
            messageTransportDto.setDesripion(COMMAND_NOT_RECOGNIZED_ERROR.getValue());
            return messageTransportDto;
        }
    }
}

