package com.iwahare.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iwahare.enums.ReservedWordsEnum;

import java.util.*;
import java.util.stream.Collectors;

import static com.iwahare.enums.ReservedWordsEnum.EMOGI_MONEY_PACK;

public class Product {
    private String name;
    private Integer price;
    private List<Extra> extras;
    private String extraDescription;
    private String preNameEmoji;
    private String postNameEmoji;
    private String categoryName;

    public Product(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public Product() {
    }

    public void setPreNameEmoji(String preNameEmoji) {
        this.preNameEmoji = preNameEmoji;
    }

    public Product(Product product) {
        this.name = (product.getName());
        this.price = (product.getPrice());
        this.extras = product.getExtras();
        this.extraDescription = product.getExtraDescription();
        this.preNameEmoji = product.getPreNameEmoji();
        this.postNameEmoji=product.getPostNameEmoji();
        this.categoryName=product.getCategoryName();
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public void addExtra(Extra extra) {
        if (this.extras == null) {
            extras = new ArrayList<>();
        }
        if (!extras.contains(extra)) {
            extras.add(extra);
        } else {
            extras.remove(extra);
        }
    }

    public String getPreNameEmoji() {
        return preNameEmoji;
    }

    public String getButtonName() {
        String result = new String(name);
        if (!preNameEmoji.isEmpty()) {
            result = preNameEmoji + result;
        }

        if (!postNameEmoji.isEmpty()) {
            result = result + "   " + price + postNameEmoji;
        }

        return result;

    }

    public String getButtonCallback() {
        return "/" + name;
    }


    public String getPostNameEmoji() {
        return postNameEmoji;
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
    public String toString(long count) {
        String res = this.preNameEmoji + this.categoryName + " " + this.name;
        if (count > 1) {
            res = res + "(" + "x" + count + ")";
        }
        String sufix = new String( price * count + " " + ReservedWordsEnum.UAH_TEXT.getValue());
        int len = 30 - res.length() - sufix.length();
        if (len > 0) {
            char[] data = new char[len];
            Arrays.fill(data, ' ');

            res = res + new String(data);
        }
        res = res + sufix;
        if (this.extras != null) {
            res = res + extras.stream().map(extra -> extra.toString(count)).collect(Collectors.joining());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name) &&
                Objects.equals(price, product.price) &&
                Objects.equals(extras, product.extras) &&
                Objects.equals(categoryName, product.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, extras, categoryName);
    }
}
