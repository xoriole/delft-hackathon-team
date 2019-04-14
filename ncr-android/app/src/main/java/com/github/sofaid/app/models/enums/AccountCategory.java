package com.github.sofaid.app.models.enums;

/**
 * Created by robik on 6/9/17.
 */
public enum AccountCategory {
    INDIVIDUAL(1),
    ORGANIZATION(2);
    private int id;

    AccountCategory(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static AccountCategory fromId(Integer id) {
        if (id == null) {
            return null;
        }
        AccountCategory[] values = AccountCategory.values();
        for (AccountCategory value : values) {
            if (id.equals(value.getId())) {
                return value;
            }
        }
        return null;
    }
}
