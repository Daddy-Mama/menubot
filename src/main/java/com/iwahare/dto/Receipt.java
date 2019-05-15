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

import static com.iwahare.enums.ReservedWordsEnum.TITLE_TEXT;
import static com.iwahare.enums.ReservedWordsEnum.TOTAL_TEXT;
import static com.iwahare.enums.ReservedWordsEnum.UAH_TEXT;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Receipt {

    private final String title = TITLE_TEXT.getValue();

    private List<Product> orders = new ArrayList<>();

    public Receipt() {
    }

    public void addOrder(Product product) {
        this.orders.add(product);
    }

    public String toPayment(){
      return   IntStream.range(0, orders.size())
                .mapToObj(i -> i + 1 + ". " + orders.get(i).toString())
                .collect(Collectors.joining("\n"));
    }
    public synchronized String toString() {
        Integer summary = orders.stream()
                .map(x -> x.getPrice())
                .reduce(0, (sum, x) -> sum + x);

        Integer extrasSum = orders.stream()
                .map(Product::getExtras)
                .filter(extras -> extras != null)
                .flatMap(extras -> extras.stream())
                .map(extra -> extra.getPrice())
                .reduce(0, (s, price) -> s + price);
        if (extrasSum == null) {
            extrasSum = 0;
        }

        summary = summary + extrasSum;
        return "==========================="
                + "\n"
                + title
                + "\n\n "
                + IntStream.range(0, orders.size())
                .mapToObj(i -> i + 1 + ". " + orders.get(i).toString())
                .collect(Collectors.joining("\n"))
                + "\n\n"
                + TOTAL_TEXT.getValue()
                + summary
                + UAH_TEXT.getValue()

                + "==========================="
                + "\n\n";

    }

    public static String toJson(Receipt receipt) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(receipt);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    public static Receipt fromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, Receipt.class);
        } catch (IOException e) {
            return null;
        }
    }

    public String getTitle() {
        return title;
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
