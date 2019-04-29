package com.iwahare.message;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class NotificationMessage {
    private SendMessage sendMessage;

    public NotificationMessage(String user, Long chatId) {
        this.sendMessage = new SendMessage();
        this.sendMessage.setChatId(chatId);
    }

    public SendMessage getSendMessage() {
        return sendMessage;
    }
}
