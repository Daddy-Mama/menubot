package com.iwahare.impl;

 import com.iwahare.message.MessageTransportDto;
 import org.telegram.telegrambots.meta.api.objects.Update;

public interface IRequestService {
    MessageTransportDto operatePayment(Update update);
//    MessageTransportDto operateSuccessfullPayment(Update update);
    MessageTransportDto operateCallbackQuery(Update update);
    MessageTransportDto operateMessage(Update update);

}
