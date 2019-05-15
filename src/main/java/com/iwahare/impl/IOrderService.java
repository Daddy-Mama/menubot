package com.iwahare.impl;

import com.iwahare.message.MessageTransportDto;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

/**
 * Created by Артем on 08.05.2019.
 */
public interface IOrderService {
    MessageTransportDto buildOrderMenu(Integer userId);
    MessageTransportDto operateCallback(List<String> callback, User user);
}