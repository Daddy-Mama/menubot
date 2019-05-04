package com.iwahare.impl;

import com.iwahare.dto.Extra;
import com.iwahare.dto.Product;
import com.iwahare.receipt.Receipt;

public interface IDataBaseService {
    void saveReceipt(Integer key, Receipt receipt);

    void deleteReceipt(Integer key);

    Receipt getReceiptByUser(Integer key);

    Receipt buildReceipt(Integer key, Product product);

//    Receipt buildReceipt(Integer key, Product product, Extra extra);

    Receipt replaceLastProduct(Integer key, Product product);
}