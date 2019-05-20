package com.iwahare.impl;

import com.iwahare.dto.PayedOrderInfo;
import com.iwahare.dto.Receipt;
import com.iwahare.message.MessageTransportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.iwahare.enums.CommandsEnum.*;
import static com.iwahare.enums.ReservedWordsEnum.*;

/**
 * Created by Артем on 08.05.2019.
 */
@Service
public class OrderService implements IOrderService {
    @Autowired
    private IKeyboardService keyboardService;
    @Autowired
    private IDataBaseService dataBaseService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IReceiptService receiptService;

    @Value(value = "${telegram.payment.token}")
    private String paymentToken;

    @Override
    public MessageTransportDto operatePayment(Update update) {
        User user = update.getMessage().getFrom();
//        dataBaseService.deleteReceipt(user.getId());
        PayedOrderInfo payedOrderInfo = new PayedOrderInfo();
        DeleteMessage deleteMessage = new DeleteMessage();

        deleteMessage.setMessageId(Integer.valueOf(update.getMessage().getSuccessfulPayment().getInvoicePayload()));

        payedOrderInfo.setDeleteMessage(deleteMessage);
        payedOrderInfo.setNotification(receiptService.toChiefForm(dataBaseService.deleteReceipt(user.getId())));

        MessageTransportDto messageTransportDto = buildOrderMenu(user.getId());


        messageTransportDto.setPayedOrderInfo(payedOrderInfo);
        return messageTransportDto;

    }

    @Override
    public MessageTransportDto operateCallback(List<String> callback, User user, Update update) {
        if (callback.contains(ORDER_MENU_CLEAR_BUTTON_CALLBACK.getValue())) {
            return operateClearCommand(user);
        }
        if (callback.contains(ORDER_MENU_PAY_BUTTON_CALLBACK.getValue())) {
            return operatePaymentRequest(user);
        }
        if (callback.contains(ORDER_MENU_SET_TIME_BUTTON_CALLBACK.getValue())) {
            return buildTimeSetterMenu(user);
        }
        if (callback.contains(ORDER_MENU_CALLBACK.getValue()) && callback.size() == 1) {
            return buildOrderMenu(user.getId());
        }
        if (callback.contains(ORDER_MENU_ADD_TIME_BUTTON_CALLBACK.getValue())) {
            return addTimeAndReturn(user, callback.get(callback.size() - 1));
        }
        if (callback.contains(ORDER_MENU_ADD_COMMENT_BUTTON_CALLBACK.getValue())) {
            return setCommentAvailable(user, update);
        }
        return null;
    }

    public MessageTransportDto operateComment(Integer messageId, String message, User user) {
        Receipt receipt = dataBaseService.getReceiptByUser(user.getId());
        if (receipt.isCommentAvailable()) {
            receipt.setComment(message);
            List<DeleteMessage> deleteMessages = new ArrayList<>();
            deleteMessages.add(new DeleteMessage().setMessageId(receipt.getReceiptMessageId()));
            deleteMessages.add(new DeleteMessage().setMessageId(messageId
            ));
            MessageTransportDto messageTransportDto = buildOrderMenu(user.getId());
            messageTransportDto.setDeleteMessage(deleteMessages);

            return messageTransportDto;
        }

        return null;
    }

    private MessageTransportDto setCommentAvailable(User user, Update update) {
        MessageTransportDto messageTransportDto = new MessageTransportDto();
        Receipt receipt = dataBaseService.getReceiptByUser(user.getId());
        receipt.setCommentAvailable(true);
        receipt.setReceiptMessageId(update.getCallbackQuery().getMessage().getMessageId());


        List<String> buttonNames = new ArrayList<>();
        List<String> buttonCallback = new ArrayList<>();

        buttonNames.add(CANCEL_BUTTON_TEXT.getValue());
        buttonCallback.add(ORDER_MENU_CALLBACK.getValue());


        messageTransportDto.setReceiptText(receiptService.toCustomerForm(receipt));
        messageTransportDto.setInlineKeyboardMarkup(keyboardService.buildInlineKeyboard(buttonNames, buttonCallback));
        messageTransportDto.setDesripion(INPUT_COMMENT_TEXT.getValue());

        return messageTransportDto;
    }

    private MessageTransportDto addTimeAndReturn(User user, String time) {
        Receipt receipt = dataBaseService.getReceiptByUser(user.getId());
        receipt.setTime(time);
        return buildTimeSetterMenu(user);
    }

    private MessageTransportDto buildTimeSetterMenu(User user) {

        MessageTransportDto messageTransportDto = new MessageTransportDto();
        List<String> buttonNames = new ArrayList<>();
        List<String> buttonCallback = new ArrayList<>();
        Receipt receipt = dataBaseService.getReceiptByUser(user.getId());

        buttonNames.add(GET_AFTER_TEXT.getValue() + TAKE_IN_15_MINS.getValue() + MINS_TEXT.getValue());
        buttonNames.add(GET_AFTER_TEXT.getValue() + TAKE_IN_30_MINS.getValue() + MINS_TEXT.getValue());
        buttonNames.add(GET_AFTER_TEXT.getValue() + TAKE_IN_1_HOUR.getValue() + HOUR_TEXT.getValue());

        buttonNames.add(BACK_TEXT.getValue());

        buttonCallback.add(ORDER_MENU_CALLBACK.getValue() + "/" + ORDER_MENU_ADD_TIME_BUTTON_CALLBACK.getValue() + "/" + FIFTHTEEN_MINS.getValue());
        buttonCallback.add(ORDER_MENU_CALLBACK.getValue() + "/" + ORDER_MENU_ADD_TIME_BUTTON_CALLBACK.getValue() + "/" + THIRTY_MINS.getValue());
        buttonCallback.add(ORDER_MENU_CALLBACK.getValue() + "/" + ORDER_MENU_ADD_TIME_BUTTON_CALLBACK.getValue() + "/" + SIXTY_MINS.getValue());
        buttonCallback.add(ORDER_MENU_CALLBACK.getValue());

        messageTransportDto.setReceiptText(receiptService.toCustomerForm(receipt));
        messageTransportDto.setInlineKeyboardMarkup(keyboardService.buildInlineKeyboard(buttonNames, buttonCallback));
        return messageTransportDto;
    }

    private MessageTransportDto operateClearCommand(User user) {
        dataBaseService.deleteReceipt(user.getId());
        return menuService.buildMainMenu(user.getId());
    }

    private MessageTransportDto operatePaymentRequest(User user) {
        MessageTransportDto messageTransportDto = new MessageTransportDto();
        Receipt receipt = dataBaseService.getReceiptByUser(user.getId());
        SendInvoice sendInvoice = new SendInvoice();
        sendInvoice.setTitle(RECEIPT_TITLE.getValue());
        sendInvoice.setDescription(receiptService.toCustomerForm(receipt));
        sendInvoice.setPayload("");
        sendInvoice.setProviderToken(paymentToken);
        sendInvoice.setStartParameter("asf123asg");
        sendInvoice.setCurrency(PAYMENT_CURRENCY.getValue());
        sendInvoice.setPrices(receiptService.getLabeledPrice(receipt));
        messageTransportDto.setSendInvoice(sendInvoice);

        return messageTransportDto;
    }


    @Override
    public MessageTransportDto buildOrderMenu(Integer userId) {
        MessageTransportDto messageTransportDto = new MessageTransportDto();
        List<String> buttonNames = new ArrayList<>();
        List<String> buttonCallback = new ArrayList<>();

        Receipt receipt = dataBaseService.getReceiptByUser(userId);
        if (receipt != null) {

            buttonNames.add(ORDER_MENU_PAY_BUTTON.getValue());
            buttonNames.add(ORDER_MENU_CANCEL_BUTTON.getValue());
            buttonNames.add(ORDER_MENU_SET_TIME_BUTTON.getValue());
            buttonNames.add(COMMENT_TEXT.getValue());

            buttonNames.add(BACK_TEXT.getValue());


            buttonCallback.add(ORDER_MENU_CALLBACK.getValue() + "/" + ORDER_MENU_PAY_BUTTON_CALLBACK.getValue());
            buttonCallback.add(ORDER_MENU_CALLBACK.getValue() + "/" + ORDER_MENU_CLEAR_BUTTON_CALLBACK.getValue());
            buttonCallback.add(ORDER_MENU_CALLBACK.getValue() + "/" + ORDER_MENU_SET_TIME_BUTTON_CALLBACK.getValue());
            buttonCallback.add(ORDER_MENU_CALLBACK.getValue() + "/" + ORDER_MENU_ADD_COMMENT_BUTTON_CALLBACK.getValue());
            buttonCallback.add(BACK_TO_MENU_CALLBACK.getValue());
            messageTransportDto.setReceiptText(receiptService.toCustomerForm(receipt));
            messageTransportDto.setInlineKeyboardMarkup(keyboardService.buildInlineKeyboard(buttonNames, buttonCallback));
            return messageTransportDto;
        } else {
            return menuService.buildMainMenu(userId);
        }
    }
}