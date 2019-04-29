package com.iwahare.message;

import com.iwahare.receipt.Receipt;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageTransportDto {
    //    private SendMessage sendMessage;
//    private SendPhoto sendPhoto;
//    private EditMessageText editMessageText;
//    private DeleteMessage deleteMessage;
//    private SendInvoice sendInvoice;
//    private AnswerPreCheckoutQuery answerPreCheckoutQuery;
//    private  NotificationMessage notificationMessage;
    private String desripion;
    private List<String> inlineKeyboardMarkup;
    private String photoId;
    private Receipt receipt;

    public MessageTransportDto() {
    }

    public MessageTransportDto(String desripion, String photoId, Receipt receipt, List<String> inlineKeyboardMarkup) {
        this.desripion = desripion;
        this.inlineKeyboardMarkup = inlineKeyboardMarkup;
        this.photoId = photoId;
        this.receipt = receipt;
    }

    public String getText() {
        String text = desripion;
        if (receipt != null) {
            text = text + receipt.toString();
        }
        return text;
    }

    public InlineKeyboardMarkup getInlineKeyboard() {
        List<List<InlineKeyboardButton>> buttons =
                inlineKeyboardMarkup.stream()
                        .map(x -> new InlineKeyboardButton(x).setCallbackData("/"+x+"/"+receipt.toJson()))
                        .map(x -> {
                            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
                            keyboardButtonsRow.add(x);
                            return keyboardButtonsRow;
                        })
                        .collect(Collectors.toList());

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(buttons);
        return keyboardMarkup;
    }

    public String getDesripion() {
        return desripion;
    }

    public void setDesripion(String desripion) {
        this.desripion = desripion;
    }

    public List<String> getInlineKeyboardMarkup() {
        return inlineKeyboardMarkup;
    }

    public void setInlineKeyboardMarkup(List<String> inlineKeyboardMarkup) {
        this.inlineKeyboardMarkup = inlineKeyboardMarkup;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }
//    public AnswerPreCheckoutQuery getAnswerPreCheckoutQuery() {
//        return answerPreCheckoutQuery;
//    }
//
//    public void setAnswerPreCheckoutQuery(AnswerPreCheckoutQuery answerPreCheckoutQuery) {
//        this.answerPreCheckoutQuery = answerPreCheckoutQuery;
//    }
//
//    public NotificationMessage getNotificationMessage() {
//        return notificationMessage;
//    }
//
//    public void setNotificationMessage(NotificationMessage notificationMessage) {
//        this.notificationMessage = notificationMessage;
//    }
//
//    public DeleteMessage getDeleteMessage() {
//        return deleteMessage;
//    }
//
//    public void setDeleteMessage(DeleteMessage deleteMessage) {
//        this.deleteMessage = deleteMessage;
//    }
//
//    public SendInvoice getSendInvoice() {
//        return sendInvoice;
//    }
//
//    public void setSendInvoice(SendInvoice sendInvoice) {
//        this.sendInvoice = sendInvoice;
//    }
//
//    public SendMessage getSendMessage() {
//        return sendMessage;
//    }
//
//    public void setSendMessage(SendMessage sendMessage) {
//        this.sendMessage = sendMessage;
//    }
//
//    public SendPhoto getSendPhoto() {
//        return sendPhoto;
//    }
//
//    public void setSendPhoto(SendPhoto sendPhoto) {
//        this.sendPhoto = sendPhoto;
//    }
//
//    public EditMessageText getEditMessageText() {
//        return editMessageText;
//    }
//
//    public void setEditMessageText(EditMessageText editMessageText) {
//        this.editMessageText = editMessageText;
//    }
}
