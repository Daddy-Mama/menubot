package com.iwahare.enums;

public enum CommandsEnum {
    START_TEXT("/start"),
    CATEGORY_TEXT("/category/"),
    RECEIPT_TEXT("/receipt/"),
    PRODUCT_TEXT("/product/"),
    EXTRAS_TEXT("/extras/"),
    OK_TEXT("finalize"),
    BACK_TEXT("back"),
    OK_CALLBACK_TEXT("/finalize"),
    BACK_CALLBACK_TEXT("/back"),
    MAKE_ORDER_TEXT("Сделать заказ"),

    PAY_TEXT("Оплатить"),

    CANCEL_TEXT("Отмена"),

    MENU_TEXT("Menu"),

    COMMAND_NOT_RECOGNIZED_ERROR("Command not recognized");


    private String value;

    private CommandsEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
