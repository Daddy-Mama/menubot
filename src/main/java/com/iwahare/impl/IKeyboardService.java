package com.iwahare.impl;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface IKeyboardService{
    ReplyKeyboardMarkup getKeyboard(String key);
}