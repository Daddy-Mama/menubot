package com.iwahare.dto;

import static com.iwahare.enums.ReservedWordsEnum.UAH_TEXT;

public class Extra {
    private String name;
    private Integer price;

    public Extra() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "\n   + " + this.name + " - " + price + " " + UAH_TEXT.getValue();
    }
}