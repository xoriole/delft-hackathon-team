package com.github.sofaid.app.ui.send_ncr;

import com.github.sofaid.app.models.internal.TransactionAmount;

/**
 * Created by robik on 6/17/17.
 */

public class SendNcrForm {
    private Integer toIdType;
    private String recepientId;
    private TransactionAmount transactionAmount;
    private String remarks;

    public Integer getToIdType() {
        return toIdType;
    }

    public void setToIdType(Integer toIdType) {
        this.toIdType = toIdType;
    }

    public String getRecepientId() {
        return recepientId;
    }

    public void setRecepientId(String recepientId) {
        this.recepientId = recepientId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public TransactionAmount getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(TransactionAmount transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
}
