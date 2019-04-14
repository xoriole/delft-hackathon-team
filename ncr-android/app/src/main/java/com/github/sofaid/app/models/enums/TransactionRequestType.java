package com.github.sofaid.app.models.enums;

/**
 * Created by robik on 6/9/17.
 */
public enum TransactionRequestType {
    FUND_TRANSFER(1),
    TOPUP(2),
    MERCHANT_PAYMENT(3),
    WITHDRAW_FROM_BANK(4);
    private int id;

    TransactionRequestType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static TransactionRequestType fromId(Integer id) {
        if (id == null) {
            return null;
        }
        TransactionRequestType[] values = TransactionRequestType.values();
        for (TransactionRequestType value : values) {
            if (id.equals(value.getId())) {
                return value;
            }
        }
        return null;
    }
}
