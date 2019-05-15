package com.iwahare.impl;

import com.iwahare.dto.Menu;
import com.iwahare.dto.MenuBuilder;
import com.iwahare.dto.Product;
import com.iwahare.message.MessageTransportDto;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

/**
 * Created by Артем on 03.05.2019.
 */
public interface IMenuService {
    MessageTransportDto operateCallback(List<String> callback,User user);
    MessageTransportDto operateProduct(String categoryName, String productName, Integer userId);
    MessageTransportDto operateCategory(String categoryName, Integer userId);
    MessageTransportDto operateExtra(String categoryName,String productName, Integer userId, String extraName);
    MessageTransportDto buildCategory(Menu category);
    MessageTransportDto buildProductMenu(Menu category, Product product);
    MessageTransportDto buildMainMenu(Integer userId);
    MessageTransportDto operateBackCommand(String categoryName, Integer userId);
}