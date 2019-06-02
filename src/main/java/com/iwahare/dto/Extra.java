package com.iwahare.dto;

import com.iwahare.enums.ReservedWordsEnum;

import java.util.Arrays;
import java.util.Collections;

import static com.iwahare.enums.ReservedWordsEnum.EMOGI_MONEY_PACK;
import static com.iwahare.enums.ReservedWordsEnum.UAH_TEXT;

public class Extra {
    private String name;
    private Integer price;
    private String preNameEmoji;

    public Extra() {
    }

    public String getPreNameEmoji() {
        return preNameEmoji;
    }


    public String getButtonName() {
        String result = new String(name);
        if (!preNameEmoji.isEmpty()) {
            result = preNameEmoji + result;
        }
        result = result + "   " + price + EMOGI_MONEY_PACK.getValue();
        return result;

    }

    public String getButtonCallback() {
        return "/" + name;
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
        String result = new String(name);
        if (!preNameEmoji.isEmpty()) {
            result = preNameEmoji + result;
        }
        result = "   +  " + result;
        String sufix = new String(  EMOGI_MONEY_PACK.getValue()+ price + " " + ReservedWordsEnum.UAH_TEXT.getValue());
//        int len = 45 - result.length() - sufix.length();
//        if (len>0){
//            char[] data = new char[len];
//            Arrays.fill(data, ' ');
//
//            result = result + new String(data);
//        }
        result = "\n"+ result +"   "+ sufix;

        return result;
    }
}