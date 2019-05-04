package com.iwahare.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iwahare.enums.ReservedWordsEnum;

import java.util.List;
import java.util.stream.Collectors;

public class Product {
    private String name;
    private Integer price;
    private List<Extra> extras;
    private String extraDescription;

    public Product(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public Product() {
    }

    public Product(Product product) {
        this.name = (product.getName());
        this.price = (product.getPrice());
        this.extras = product.getExtras();
        this.extraDescription = product.getExtraDescription();
    }

    public String getExtraDescription() {
        return extraDescription;
    }

    public void setExtraDescription(String extraDescription) {
        this.extraDescription = extraDescription;
    }

    public List<Extra> getExtras() {
        return extras;
    }

    public void setExtras(List<Extra> extras) {
        this.extras = extras;
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

    @JsonIgnore
    @Override
    public String toString() {
        String res = this.name + " - " + price + " " + ReservedWordsEnum.UAH_TEXT.getValue();
        if (this.extras != null) {
            res = res + extras.stream().map(Extra::toString).collect(Collectors.joining());
        }
        return res;
    }

    @JsonIgnore
    public String getExtraUrl() {
        String res = "";
        if (this.extras != null) {
            res = res + this.extras.stream()
                    .map(Extra::getName)
                    .collect(Collectors.joining("/"));
        }
        return res;
    }
}
