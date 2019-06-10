package com.iwahare.impl;

import com.iwahare.dto.Extra;
import com.iwahare.dto.Product;
import com.iwahare.dto.Receipt;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.iwahare.enums.ReservedWordsEnum.*;

@Service
public class ReceiptService implements IReceiptService {

    public Integer getTotalPrice(Receipt receipt) {
        Integer summary = receipt.getOrders().stream()
                .map(x -> x.getPrice())
                .reduce(0, (sum, x) -> sum + x);

        Integer extrasSum = receipt.getOrders().stream()
                .map(Product::getExtras)
                .filter(extras -> extras != null)
                .flatMap(extras -> extras.stream())
                .map(extra -> extra.getPrice())
                .reduce(0, (s, price) -> s + price);
        if (extrasSum == null) {
            extrasSum = 0;
        }
        summary = summary + extrasSum;
        return summary;
    }

    public String toChiefForm(Receipt receipt) {
        if (receipt == null) {
            return null;
        }
        Integer summary = getTotalPrice(receipt);
        return DIVIDER_TEXT.getValue()
                + "\n"
                + CHIEF_TITLE_TEXT.getValue()
                + buildReceiptBody(receipt)
                + "\n"
                + ADDITIONAL_INFO_TEXT.getValue() + "\n"

                + buildTakeTime(receipt.getTime()) + "\n"
                + buildComment(receipt)

                + "\n\n"
                + buildTotal(summary)
                + "\n"
                + DIVIDER_TEXT.getValue()
                + "\n\n";
    }

    private String buildReceiptBody(Receipt receipt) {
        String stringReceipt = "";
//                + IntStream.range(0, receipt.getOrders().size())
//                .mapToObj(i -> i + 1 + ". " + receipt.getOrders().get(i).toString())
//                .collect(Collectors.joining("\n"))
//                + "\n";
        Set<Product> productSet = new HashSet<>();
        int i = 0;
        for (Product p : receipt.getOrders()) {

            if (productSet.add(p)) {
                i++;
                long count = receipt.getOrders().stream()
                        .filter(product -> product.equals(p))
                        .count();
                stringReceipt = stringReceipt + i + ". " + p.toString(count) + "\n";

            }

        }
        stringReceipt = stringReceipt + "\n";
        return stringReceipt;
    }

    private String buildTakeTime(String time) {
        return GET_AFTER_TEXT.getValue() + time;
    }

    private String buildTotal(Integer summary) {
        return TOTAL_TEXT.getValue()
                + summary
                + UAH_TEXT.getValue();
    }

    public String toCustomerForm(Receipt receipt) {
        if (receipt == null) {
            return null;
        }
        Integer summary = getTotalPrice(receipt);
        return DIVIDER_TEXT.getValue()
                + "\n"
                + TITLE_TEXT.getValue()
                + "\n"
                + buildReceiptBody(receipt)
                + "\n"
                + ADDITIONAL_INFO_TEXT.getValue() + "\n"
                + buildTakeTime(receipt.getTime()) + "\n"
                + buildComment(receipt)

                + "\n\n"
                + buildTotal(summary)
                + "\n"
                + DIVIDER_TEXT.getValue()
                + "\n\n";
    }

    private String buildComment(Receipt receipt) {
        if (receipt.getComment() != null) {
            return COMMENT_TEXT.getValue() + " " + receipt.getComment() + "\n";
        }
        return "";
    }

    public List<LabeledPrice> getLabeledPrice(Receipt receipt) {
        List<LabeledPrice> labeledPrices = new ArrayList<>();
        receipt.getOrders().stream()
                .forEach(product -> labeledPrices.add(new LabeledPrice(product.getName(), product.getPrice() * 100)));
        receipt.getOrders().stream()
                .filter(product -> product.getExtras() != null)
                .map(Product::getExtras)
                .flatMap(extras -> extras.stream())
                .forEach(extra -> labeledPrices.add(new LabeledPrice(extra.getName(), extra.getPrice() * 100)));
        return labeledPrices;
    }
}
