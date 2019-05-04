package com.iwahare.impl;

import com.iwahare.dto.Menu;
import com.iwahare.dto.MenuBuilder;
import com.iwahare.dto.Product;
import com.iwahare.message.MessageTransportDto;

import java.util.List;

/**
 * Created by Артем on 03.05.2019.
 */
public interface IMenuService {
    MessageTransportDto operateProduct(String categoryName, String productName, Integer userId);
    MessageTransportDto operateCategory(String categoryName, Integer userId);
    MessageTransportDto operateExtra(String categoryName,String productName, Integer userId,List<String> extraName);
    MessageTransportDto buildCategory(Menu category);
    MessageTransportDto buildProductMenu(Menu category, Product product);
    MessageTransportDto buildMainMenu();
}