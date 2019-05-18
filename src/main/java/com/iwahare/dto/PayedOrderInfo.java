package com.iwahare.dto;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;

public class PayedOrderInfo {
    private DeleteMessage deleteMessage;
    private String notification;

    public PayedOrderInfo() {
    }

    public DeleteMessage getDeleteMessage() {
        return deleteMessage;
    }

    public void setDeleteMessage(DeleteMessage deleteMessage) {
        this.deleteMessage = deleteMessage;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }
}
