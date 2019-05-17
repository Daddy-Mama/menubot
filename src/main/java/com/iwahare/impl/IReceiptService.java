package com.iwahare.impl;

import com.iwahare.dto.Receipt;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;

import java.util.List;

public interface IReceiptService {
    String toChiefForm(Receipt receipt);
    String toCustomerForm(Receipt receipt);
    List<LabeledPrice> getLabeledPrice(Receipt receipt);
    Integer getTotalPrice(Receipt receipt);
}
