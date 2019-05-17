package com.iwahare.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.iwahare.enums.ReservedWordsEnum.*;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Receipt {


    private List<Product> orders = new ArrayList<>();

    public Receipt() {
    }

    public void addOrder(Product product) {
        this.orders.add(product);
    }

    public List<Product> getOrders() {
        return orders;
    }

    public void setOrders(List<Product> orders) {
        this.orders = orders;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return this.orders.size() == 0;
    }
}
