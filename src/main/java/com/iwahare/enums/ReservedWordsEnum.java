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
    COMMENT_TEXT("Комментарий к заказу:"),
    MINS_TEXT(" минут"),
    HOUR_TEXT(" час"),
    MENU_ADDITIONAL_INFO_TEXT("Menu additional info"),
    ADDITIONAL_INFO_TEXT("Дополнительная информация: "),
    CUSTOMER_MAIN_MENU("Customer main menu"),
    CUSTOMER_PAYMENT_MENU("Customer payment menu"),
    RECEIPT_TITLE("Оплата заказа"),
    INPUT_COMMENT_TEXT("Напишите комментарий к заказу"),
    EMPTY_RECEIPT_ERROR_TEXT("You have not made order yet!"),
    CUSTOMER_RECEIPT_BUTTON("My order"),
    CUSTOMER_MENU_BUTTON("Menu"),

    CANCEL_BUTTON_TEXT("Отменить"),

    ORDER_MENU_PAY_BUTTON("Pay"),
    ORDER_MENU_CANCEL_BUTTON("Cancel"),
    ORDER_MENU_SET_TIME_BUTTON("Указать время"),
    ORDER_MENU_BUTTON("Operate order"),


    /////////////////////////////////////////////TIME
    GET_AFTER_TEXT("Забрать через "),

    TAKE_IN_5_MINS("5"),
    TAKE_IN_15_MINS("15"),
    TAKE_IN_30_MINS("30"),
    TAKE_IN_1_HOUR("1" ),

    ;
    private String value;

    private ReservedWordsEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
