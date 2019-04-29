package com.iwahare.impl;

import com.iwahare.dto.Menu;
import com.iwahare.dto.Product;
import com.iwahare.message.MessageTransportDto;
import com.iwahare.receipt.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.iwahare.enums.CommandsEnum.CANCEL_TEXT;
import static com.iwahare.enums.CommandsEnum.PAY_TEXT;
import static com.iwahare.enums.CommandsEnum.START_TEXT;
import static com.iwahare.enums.ReservedWordsEnum.HELLO_TEXT;

@Service
public class RequestService implements IRequestService {
    private final List<Integer> adminChatList = Arrays.asList();
    private Map<Receipt, User> receipts = new ConcurrentHashMap<>();

    @Autowired
    private Menu menu;

    @Override
    public MessageTransportDto operatePayment(Update update) {
        return null;
    }

    @Override
    public MessageTransportDto operateCallbackQuery(Update update) {
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
            return buildMenu(menu, null);
        } else {
            return new MessageTransportDto();
        }
    }

    private MessageTransportDto buildMenu(Menu menu, Receipt receipt) {
        List<String> keyboardIcons = new ArrayList<>();
        if (!menu.getCategories().isEmpty()) {
            keyboardIcons.addAll(menu.getCategories().stream()
                    .map(Menu::getName)
                    .collect(Collectors.toList()));
        }
        if (!menu.getProducts().isEmpty()) {
            keyboardIcons.addAll(menu.getProducts().stream()
                    .map(Product::getName)
                    .collect(Collectors.toList()));
        }
        MessageTransportDto messageTransportDto = new MessageTransportDto(menu.getDescription(), menu.getPhotoId(), receipt, keyboardIcons);
        return messageTransportDto;
    }
}
