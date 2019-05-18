package com.iwahare.enums;

public enum CommandsEnum {
    START_TEXT("/start"),
    CATEGORY_TEXT("/category/"),
    RECEIPT_TEXT("/receipt/"),
    PRODUCT_TEXT("/product/"),
    EXTRAS_TEXT("/extras/"),
    OK_TEXT("Ok"),
    BACK_TEXT("back"),

    ORDER_MENU_ADD_COMMENT_BUTTON_CALLBACK("add-comment"),
    ORDER_MENU_CALLBACK("order"),
    ORDER_MENU_PAY_BUTTON_CALLBACK("pay"),
    ORDER_MENU_CLEAR_BUTTON_CALLBACK("clear"),
    ORDER_MENU_SET_TIME_BUTTON_CALLBACK("set-time"),
    ORDER_MENU_ADD_TIME_BUTTON_CALLBACK("add-time"),
    FIFTHTEEN_MINS("15 минут"),
    THIRTY_MINS("30 минут"),
    SIXTY_MINS("1 час"),

    MAKE_ORDER_TEXT("Сделать заказ"),

    PAY_TEXT("Оплатить"),

    CANCEL_TEXT("Отмена"),

    BACK_TO_MENU_CALLBACK("menu"),

    COMMAND_NOT_RECOGNIZED_ERROR("Command not recognized");


    private String value;

    private CommandsEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
