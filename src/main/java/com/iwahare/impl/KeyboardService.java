package com.iwahare.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.iwahare.enums.ReservedWordsEnum.*;

@Component
public class KeyboardService implements IKeyboardService {
    private final ReplyKeyboardMarkup keyboardMarkup;

    @Autowired
    public KeyboardService() {
        keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton(CUSTOMER_MENU_BUTTON.getValue()));
        row.add(new KeyboardButton(CUSTOMER_RECEIPT_BUTTON.getValue()));
        rows.add(row);

        keyboardMarkup.setKeyboard(rows);
        keyboardMarkup.setResizeKeyboard(true);
    }

    public ReplyKeyboardMarkup getKeyboard() {
        return keyboardMarkup;
    }
}