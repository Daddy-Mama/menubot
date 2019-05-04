package com.iwahare.dto;

import java.util.List;

/**
 * Created by Артем on 03.05.2019.
 */
public class MenuBuilder {
    private Menu category;
    private Product product;
    private List<Extra> extras;

    public List<Extra> getExtras() {
        return extras;
    }

    public void setExtras(List<Extra> extras) {
        this.extras = extras;
    }

    public MenuBuilder() {
    }

    public MenuBuilder(Menu category, Product product) {
        this.category = category;
        this.product = product;
    }

    public Menu getCategory() {
        return category;
    }

    public void setCategory(Menu category) {
        this.category = category;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}