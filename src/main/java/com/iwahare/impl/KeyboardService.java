package com.iwahare.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;

import static com.iwahare.enums.CommandsEnum.OK_TEXT;
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

    public InlineKeyboardMarkup buildInlineKeyboard(List<String> keyboardIcons, List<String> keyboardCallback) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        assert keyboardCallback.size() == keyboardIcons.size();
        Iterator<String> iteratorIcon = keyboardIcons.iterator();
        Iterator<String> iteratorCallback = keyboardCallback.iterator();
        while (iteratorIcon.hasNext()) {
            String buttonName = iteratorIcon.next();
            InlineKeyboardButton button = new InlineKeyboardButton(buttonName);
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();

            button.setCallbackData(iteratorCallback.next());
            keyboardButtonsRow.add(button);

            if (buttonName.equals(OK_TEXT.getValue())) {
                buttonName = iteratorIcon.next();
                InlineKeyboardButton backButton = new InlineKeyboardButton(buttonName);
                backButton.setCallbackData(iteratorCallback.next());
                keyboardButtonsRow.add(backButton);
                buttons.add(keyboardButtonsRow);
                break;
            }

            buttons.add(keyboardButtonsRow);


        }
        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }
}