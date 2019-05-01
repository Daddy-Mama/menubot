package com.iwahare.enums;

public enum ReservedWordsEnum {
    TITLE_TEXT("Ваш заказ: "),
    TOTAL_TEXT("Всего к оплате: "),
    UAH_TEXT("грн. "),
    HELLO_TEXT("Добро пожаловать!"),


    MENU_TITLE_TEXT("Menu title"),
    MENU_ADDITIONAL_INFO_TEXT("Menu additional info"),

    CUSTOMER_MAIN_MENU("Customer main menu"),
    CUSTOMER_PAYMENT_MENU("Customer payment menu"),

    CUSTOMER_MENU_BUTTON("Menu"),
    CUSTOMER_PAY_BUTTON("Pay"),
    CUSTOMER_CANCEL_BUTTON("Cancel");
    private String value;

    private ReservedWordsEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
