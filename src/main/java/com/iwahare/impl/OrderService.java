package com.iwahare.impl;

import com.iwahare.dto.Receipt;
import com.iwahare.message.MessageTransportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;

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
        MessageTransportDto messageTransportDto = new MessageTransportDto();
        User user = update.getMessage().getFrom();

        messageTransportDto.setDesripion(receiptService.toChiefForm(dataBaseService.deleteReceipt(user.getId())));
        return messageTransportDto;

    }

    @Override
    public MessageTransportDto operateCallback(List<String> callback, User user) {
        if (callback.contains(CUSTOMER_CLEAR_BUTTON_CALLBACK.getValue())) {
            return operateClearCommand(user);
        }
        if (callback.contains(CUSTOMER_PAY_BUTTON_CALLBACK.getValue())) {
            return operatePaymentRequest(user);
        }
        if (callback.contains(ORDER_MENU_CALLBACK.getValue()) && callback.size() == 1) {
            return buildOrderMenu(user.getId());
        }
        return null;
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
        sendInvoice.setPayload(RECEIPT_TITLE.getValue());
        sendInvoice.setProviderToken(paymentToken);
        sendInvoice.setStartParameter("asf123asg");
        sendInvoice.setCurrency(PAYMENT_CURRENCY.getValue());

        sendInvoice.setPrices(receiptService.getLabeledPrice(receipt));
        messageTransportDto.setSendInvoice(sendInvoice);
//        dataBaseService.deleteReceipt(user.getId());

        return messageTransportDto;
    }


    @Override
    public MessageTransportDto buildOrderMenu(Integer userId) {
        MessageTransportDto messageTransportDto = new MessageTransportDto();
        List<String> buttonNames = new ArrayList<>();
        List<String> buttonCallback = new ArrayList<>();
        Receipt receipt = dataBaseService.getReceiptByUser(userId);
        if (receipt != null) {

            buttonNames.add(CUSTOMER_PAY_BUTTON.getValue());
            buttonNames.add(CUSTOMER_CANCEL_BUTTON.getValue());

            buttonCallback.add(ORDER_MENU_CALLBACK.getValue() + "/" + CUSTOMER_PAY_BUTTON_CALLBACK.getValue());
            buttonCallback.add(ORDER_MENU_CALLBACK.getValue() + "/" + CUSTOMER_CLEAR_BUTTON_CALLBACK.getValue());

            messageTransportDto.setReceiptText(receiptService.toCustomerForm(receipt));
            messageTransportDto.setInlineKeyboardMarkup(keyboardService.buildInlineKeyboard(buttonNames, buttonCallback));
            return messageTransportDto;
        } else {
            messageTransportDto.setDesripion(EMPTY_RECEIPT_ERROR_TEXT.getValue());

            return messageTransportDto;
        }
    }
}