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

    private Integer countSummary() {
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
        return summary;
    }

    public synchronized String toChiefForm() {
        Integer summary = countSummary();
        return DIVIDER_TEXT.getValue()
                + "\n"
                + CHIEF_TITLE_TEXT.getValue()
                + buildReceiptBody(summary)
                + DIVIDER_TEXT.getValue()
                + "\n\n";
    }

    private String buildReceiptBody(Integer summary) {
        return "\n\n "
                + IntStream.range(0, orders.size())
                .mapToObj(i -> i + 1 + ". " + orders.get(i).toString())
                .collect(Collectors.joining("\n"))
                + "\n\n"
                + TOTAL_TEXT.getValue()
                + summary
                + UAH_TEXT.getValue();
    }

    public synchronized String toString() {
        Integer summary = countSummary();
        return DIVIDER_TEXT.getValue()
                + "\n"
                + TITLE_TEXT.getValue()
                + "\n\n "
                + IntStream.range(0, orders.size())
                .mapToObj(i -> i + 1 + ". " + orders.get(i).toString())
                .collect(Collectors.joining("\n"))
                + "\n\n"
                + TOTAL_TEXT.getValue()
                + summary
                + UAH_TEXT.getValue()

                + DIVIDER_TEXT.getValue()
                + "\n\n";

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
