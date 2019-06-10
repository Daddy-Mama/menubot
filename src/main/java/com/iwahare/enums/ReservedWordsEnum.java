package com.iwahare.enums;

import org.springframework.util.StringUtils;

import java.util.Collections;

public enum ReservedWordsEnum {

    //EMOGI CODES
    EMOGI_ADD_EXTRA("\u2714\ufe0f"),
    EMOGI_REMOVE_EXTRA("\u2716\ufe0f"),
    EMOGI_MONEY_PACK("\ud83d\udcb0"),
    EMOGI_CLOCK("\u23f0"),
    EMOGI_PINNER("\ud83d\udccc"),
    EMOGI_RING("\ud83d\udd14"),
    EMOGI_MIND_CLOUD("\ud83d\udcac"),
    EMOGI_DIVIDER(String.join("", Collections.nCopies(13, "\u2668"))),
    EMOGI_CARD("\ud83d\udcb3"),
    EMOGI_COMMENT("\ud83d\udcdd"),
    EMOGI_CANCEL("\u2757\ufe0f"),
    EMOGI_AGREE("\u2705"),
    EMOGI_BACK("\ud83d\udd19"),
    EMOGI_TRASHCAN("\ud83d\uddd1"),
    EMOGI_5_MIN("\ud83d\udd50"),
    EMOGI_15_MIN("\ud83d\udd52"),
    EMOGI_30_MIN("\ud83d\udd55"),
    EMOGI_1_HOUR("\ud83d\udd5b"),
    EMOGI_PURCASE_CAR("\ud83d\uded2"),
    // BUTTON NAMES

    CHIEF_TITLE_TEXT(EMOGI_RING.getValue() + "Поступил новый заказ" + EMOGI_RING.getValue()),
    TOTAL_TEXT(EMOGI_MONEY_PACK.getValue() + "Всего к оплате: "),
    DIVIDER_TEXT(EMOGI_DIVIDER.getValue()),
    //    MY_ORDERS_TEXT("Мой заказ"),
    //    MENU_TITLE_TEXT("Заголовок меню"),
    COMMENT_TEXT(EMOGI_COMMENT.getValue() + "Комментарий к заказу:"),
    //    MENU_ADDITIONAL_INFO_TEXT("Menu additional info"),
    INPUT_COMMENT_TEXT(EMOGI_MIND_CLOUD.getValue() + "Напишите комментарий к заказу!" + "\n" + "Здесь можно попросить поострее\ud83c\udf36, \n"+ "или упаковать 'с собой'\ud83d\udce6"),
    //    EMPTY_RECEIPT_ERROR_TEXT("You have not made order yet!"),

    CANCEL_BUTTON_TEXT(EMOGI_CANCEL.getValue() + "Отменить"),

    ORDER_MENU_PAY_BUTTON(EMOGI_CARD.getValue() + "Оплатить"),
    ORDER_MENU_CANCEL_BUTTON(EMOGI_TRASHCAN.getValue() + "Очистить"),
    ORDER_MENU_SET_TIME_BUTTON(EMOGI_CLOCK.getValue() + "Указать время"),
    ORDER_MENU_BUTTON(EMOGI_PURCASE_CAR.getValue() + "Управление заказом"+EMOGI_PURCASE_CAR.getValue()),


    //OTHER
    TITLE_TEXT("Ваш заказ:"),
    UAH_TEXT(" грн"),
    PAYMENT_CURRENCY("UAH"),
    MINS_TEXT(" минут"),
    HOUR_TEXT(" час"),
    ADDITIONAL_INFO_TEXT("Дополнительная информация: "),
    RECEIPT_TITLE(EMOGI_CARD.getValue()+"Оплата заказа"),
    CUSTOMER_RECEIPT_BUTTON("Мой заказ"),
    CUSTOMER_MENU_BUTTON("Меню"),
    WHEN_COME_TEXT(EMOGI_CLOCK.getValue()+"Когда заберете? Через: "),

    /////////////////////////////////////////////TIME
    GET_AFTER_TEXT(EMOGI_CLOCK.getValue()+ "Забрать через "),


    TAKE_IN_5_MINS("5"),
    TAKE_IN_15_MINS("15"),
    TAKE_IN_30_MINS("30"),
    TAKE_IN_1_HOUR("1"),
    ;
    private String value;

    private ReservedWordsEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
