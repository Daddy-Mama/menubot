package com.iwahare.receipt;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwahare.dto.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.iwahare.enums.ReservedWordsEnum.TITLE_TEXT;
import static com.iwahare.enums.ReservedWordsEnum.TOTAL_TEXT;
import static com.iwahare.enums.ReservedWordsEnum.UAH_TEXT;

public class Receipt {

    private final String title = TITLE_TEXT.getValue();

    private List<Product> orders = new ArrayList<>();

    public Receipt() {
    }

    public void addOrder(Product product) {
        this.orders.add(product);
    }

    public synchronized String toString() {
        return title +
                "\n\n " +
                IntStream.range(1, orders.size())
                        .mapToObj(i -> i + ". " + orders.get(i).toString())
                        .collect(Collectors.joining("\n"))
                + TOTAL_TEXT.getValue()
                + orders.stream().map(x -> x.getPrice()).reduce(0, (sum, x) -> sum + x)
                + UAH_TEXT.getValue();

    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    public boolean isEmpty() {
        return this.orders.size() == 0;
    }
}
