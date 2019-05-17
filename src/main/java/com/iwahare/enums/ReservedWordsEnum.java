package com.iwahare.enums;

public enum ReservedWordsEnum {
    TITLE_TEXT("Ваш заказ: "),
    CHIEF_TITLE_TEXT("Поступил новый заказ:"),
    TOTAL_TEXT("Всего к оплате: "),
    UAH_TEXT(" грн. "),
    PAYMENT_CURRENCY("UAH"),
    HELLO_TEXT("Добро пожаловать!"),
    DIVIDER_TEXT("==========================="),
    MY_ORDERS_TEXT("My order"),
    MENU_TITLE_TEXT("Menu title"),

    MENU_ADDITIONAL_INFO_TEXT("Menu additional info"),

    CUSTOMER_MAIN_MENU("Customer main menu"),
    CUSTOMER_PAYMENT_MENU("Customer payment menu"),
    RECEIPT_TITLE("Оплата заказа"),

    EMPTY_RECEIPT_ERROR_TEXT("You have not made order yet!"),
    CUSTOMER_RECEIPT_BUTTON("My order"),
    CUSTOMER_MENU_BUTTON("Menu"),
    CUSTOMER_PAY_BUTTON("Pay"),
    CUSTOMER_CANCEL_BUTTON("Cancel"),

    ORDER_MENU_BUTTON("Operate order");
    private String value;

    private ReservedWordsEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
