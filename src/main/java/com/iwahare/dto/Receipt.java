package com.iwahare.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.iwahare.enums.ReservedWordsEnum.*;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Receipt {


    private List<Product> orders = new ArrayList<>();
    private String time;
    private boolean commentAvailable;
    private String comment;
    private Integer receiptMessageId;
    private Calendar calendar;

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Receipt() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getReceiptMessageId() {
        return receiptMessageId;
    }

    public void setReceiptMessageId(Integer receiptMessageId) {
        this.receiptMessageId = receiptMessageId;
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

    public boolean isCommentAvailable() {
        return commentAvailable;
    }

    public void setCommentAvailable(boolean commentAvailable) {
        this.commentAvailable = commentAvailable;
    }

    public String getTime() {
        if (time==null)
            return TAKE_IN_5_MINS.getValue() + MINS_TEXT.getValue();
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return this.orders.size() == 0;
    }
}
