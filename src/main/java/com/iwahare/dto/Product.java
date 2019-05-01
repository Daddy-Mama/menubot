package com.iwahare.dto;


import com.iwahare.enums.ReservedWordsEnum;

public class Product {
    private   String name;
    private   Integer price;

    public Product(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public Product() {
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return this.name +"\n" + price + ReservedWordsEnum.UAH_TEXT;
    }
}
