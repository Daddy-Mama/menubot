package com.iwahare.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.iwahare.enums.ReservedWordsEnum.*;

@Service
public class KeyboardService implements IKeyboardService {
    private final Map<String, ReplyKeyboardMarkup> keyboardMarkupMap;

    @Autowired
    public KeyboardService() {
        keyboardMarkupMap = new HashMap<>();
        ReplyKeyboardMarkup keyboardMainMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton(CUSTOMER_MENU_BUTTON.getValue()));
        rows.add(row);
        keyboardMainMarkup.setKeyboard(rows);
        keyboardMainMarkup.setResizeKeyboard(true);
        keyboardMarkupMap.put(CUSTOMER_MAIN_MENU.getValue(), keyboardMainMarkup);

        ReplyKeyboardMarkup keyboardPaymentMarkup = new ReplyKeyboardMarkup();
        rows = new ArrayList<>();
        row = new KeyboardRow();
        row.add(new KeyboardButton(CUSTOMER_PAY_BUTTON.getValue()));
        row.add(new KeyboardButton(CUSTOMER_CANCEL_BUTTON.getValue()));
        rows.add(row);
        keyboardPaymentMarkup.setKeyboard(rows);
        keyboardPaymentMarkup.setResizeKeyboard(true);
        keyboardMarkupMap.put(CUSTOMER_PAYMENT_MENU.getValue(), keyboardPaymentMarkup);
    }

    public ReplyKeyboardMarkup getKeyboard(String key) {
        if (keyboardMarkupMap.containsKey(key)) {
            return keyboardMarkupMap.get(key);
        }
        return null;
    }
}