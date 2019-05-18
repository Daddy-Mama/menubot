package com.iwahare.message;

import com.iwahare.dto.PayedOrderInfo;
import com.iwahare.dto.Receipt;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

public class MessageTransportDto {
    private String desripion;
    private InlineKeyboardMarkup inlineKeyboardMarkup;
    private String photoId;
    private String receiptText;
    private ReplyKeyboardMarkup keyboardMarkup;
    private SendInvoice sendInvoice;
    private AnswerPreCheckoutQuery answerPreCheckoutQuery;
    private boolean sucPayment;
    private List<Long> chat_id;
    private PayedOrderInfo payedOrderInfo;

    public MessageTransportDto() {
        this.desripion = "";
    }


    public String getText() {
        String text = desripion;

        if (receiptText != null) {
            text = receiptText + text;
        }
        return text;
    }


    public PayedOrderInfo getPayedOrderInfo() {
        return payedOrderInfo;
    }

    public void setPayedOrderInfo(PayedOrderInfo payedOrderInfo) {
        this.payedOrderInfo = payedOrderInfo;
    }

    public SendInvoice getSendInvoice() {
        return sendInvoice;
    }

    public boolean isSucPayment() {
        return sucPayment;
    }

    public void setSucPayment(boolean sucPayment) {
        this.sucPayment = sucPayment;
    }

    public AnswerPreCheckoutQuery getAnswerPreCheckoutQuery() {
        return answerPreCheckoutQuery;
    }

    public void setAnswerPreCheckoutQuery(AnswerPreCheckoutQuery answerPreCheckoutQuery) {
        this.answerPreCheckoutQuery = answerPreCheckoutQuery;
    }

    public List<Long> getChat_id() {
        return chat_id;
    }

    public void setChat_id(List<Long> chat_id) {
        this.chat_id = chat_id;
    }

    public void setSendInvoice(SendInvoice sendInvoice) {
        this.sendInvoice = sendInvoice;
    }

    public String getDesripion() {
        return desripion;
    }

    public void setDesripion(String desripion) {
        this.desripion = desripion;
    }

    public InlineKeyboardMarkup getInlineKeyboardMarkup() {
        return inlineKeyboardMarkup;
    }

    public void setInlineKeyboardMarkup(InlineKeyboardMarkup inlineKeyboardMarkup) {
        this.inlineKeyboardMarkup = inlineKeyboardMarkup;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getReceiptText() {
        return receiptText;
    }

    public void setReceiptText(String receiptText) {
        this.receiptText = receiptText;
    }

    public ReplyKeyboardMarkup getKeyboardMarkup() {
        return keyboardMarkup;
    }

    public void setKeyboardMarkup(ReplyKeyboardMarkup keyboardMarkup) {
        this.keyboardMarkup = keyboardMarkup;
    }


}
