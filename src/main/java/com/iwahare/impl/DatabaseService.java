package com.iwahare.impl;

import com.iwahare.dto.Product;
import com.iwahare.dto.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseService implements IDataBaseService {
    private Map<Integer, Receipt> receiptMap;

    @Autowired
    public DatabaseService() {
        receiptMap = new HashMap<>();
    }

    public Receipt buildReceipt(Integer key, Product product) {
        Receipt receipt = getReceiptByUser(key);
        if (receipt == null) {
            receipt = new Receipt();
        }
        receipt.addOrder(product);
        saveReceipt(key, receipt);
        return receipt;
    }

    public Receipt replaceLastProduct(Integer key, Product product) {
        Receipt receipt;
        if (receiptMap.containsKey(key)) {
            receipt = receiptMap.get(key);
            receipt.getOrders().remove(receipt.getOrders().size() - 1);
            receipt.addOrder(product);
            receiptMap.replace(key, receipt);
        } else {
            receipt = buildReceipt(key, product);
        }
        return receipt;
    }

    public void saveReceipt(Integer key, Receipt receipt) {
        if (receiptMap.containsKey(key)) {
            receiptMap.replace(key, receipt);
        } else {
            receiptMap.put(key, receipt);
        }
    }

    public Receipt getReceiptByUser(Integer key) {
        if (receiptMap.containsKey(key)) {
            return receiptMap.get(key);
        }
        return null;
    }

    public void deleteReceipt(Integer key) {
        if (receiptMap.containsKey(key)) {
            receiptMap.remove(key);
        }
    }

    @Override
    public Product getLastProductInReceipt(Integer key) {
        if (receiptMap.containsKey(key)) {
            List<Product> orderList = receiptMap.get(key).getOrders();
            return orderList.get(orderList.size() - 1);
        }
        return null;
    }

    public void deleteLastProduct(Integer key) {
        if (receiptMap.containsKey(key)) {
            List<Product> orderList = receiptMap.get(key).getOrders();
            orderList.remove(orderList.size() - 1);
            if(orderList.isEmpty()){
                deleteReceipt(key);
            }
        }
    }
}