package com.github.sofaid.app.models.internal;

import java.math.BigDecimal;

/**
 * Created by robik on 6/17/17.
 */

public class TransactionAmount {
    private BigDecimal amount;
    private BigDecimal networkFeePercent;
    private BigDecimal networkFeeAmount;
    private BigDecimal serviceFeePercent;
    private BigDecimal serviceFeeAmount;
    private BigDecimal totalAmount;

    public TransactionAmount() {
    }

    public TransactionAmount(BigDecimal networkFeePercent, BigDecimal networkFeeAmount) {
        this.networkFeePercent = networkFeePercent;
        this.networkFeeAmount = networkFeeAmount;
    }

    public BigDecimal getNetworkFeePercent() {
        return networkFeePercent;
    }

    public void setNetworkFeePercent(BigDecimal networkFeePercent) {
        this.networkFeePercent = networkFeePercent;
    }

    public BigDecimal getNetworkFeeAmount() {
        return networkFeeAmount;
    }

    public void setNetworkFeeAmount(BigDecimal networkFeeAmount) {
        this.networkFeeAmount = networkFeeAmount;
    }

    public BigDecimal getServiceFeePercent() {
        return serviceFeePercent;
    }

    public void setServiceFeePercent(BigDecimal serviceFeePercent) {
        this.serviceFeePercent = serviceFeePercent;
    }

    public BigDecimal getServiceFeeAmount() {
        return serviceFeeAmount;
    }

    public void setServiceFeeAmount(BigDecimal serviceFeeAmount) {
        this.serviceFeeAmount = serviceFeeAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
