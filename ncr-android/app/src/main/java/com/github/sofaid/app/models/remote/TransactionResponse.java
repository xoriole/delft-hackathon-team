package com.github.sofaid.app.models.remote;

import java.math.BigDecimal;

/**
 * Created by robik on 6/10/17.
 */
public class TransactionResponse {
    private String transactionHash;
    private BigDecimal totalAmount;

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
