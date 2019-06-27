package com.iwahare.impl;

import com.iwahare.dto.Product;
import com.iwahare.dto.Receipt;

public interface IDataBaseService {
    void saveReceipt(Integer key, Receipt receipt);

    Receipt deleteReceipt(Integer key);

    Receipt getReceiptByUser(Integer key);

    Receipt buildReceipt(Integer key, Product product);

    Product getLastProductInReceipt(Integer key);

    Receipt replaceLastProduct(Integer key, Product product);

    void deleteLastProduct(Integer key);
    void deleteOldReceipts();
}