package com.iwahare.impl;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

public interface IKeyboardService {
    ReplyKeyboardMarkup getKeyboard();

    InlineKeyboardMarkup buildInlineKeyboard(List<String> keyboardIcons, List<String> keyboardCallback);
}