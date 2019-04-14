package com.github.sofaid.app.models.enums;

import com.github.sofaid.app.utils.StringUtil;

/**
 * Created by robik on 6/9/17.
 */
public enum AccountIdType {
    USERNAME(1, "Username", "username"),
    EMAIL(2, "Email", "email"),
    MOBILE_PHONE(3, "Mobile Phone", "mobile_phone"),
    NCR_ADDRESS(4, "NCR Address", "ncr_address");
    private int id;
    private String name;
    private String keyword;

    AccountIdType(int id, String name, String keyword) {
        this.id = id;
        this.name = name;
        this.keyword = keyword;
    }

    public int getId() {
        return id;
    }

    public static AccountIdType fromId(Integer id) {
        if (id == null) {
            return null;
        }
        AccountIdType[] values = AccountIdType.values();
        for (AccountIdType value : values) {
            if (id.equals(value.getId())) {
                return value;
            }
        }
        return null;
    }

    public static AccountIdType fromName(String name) {
        if (StringUtil.isEmpty(name)) {
            return null;
        }
        AccountIdType[] values = AccountIdType.values();
        for (AccountIdType value : values) {
            if (name.equalsIgnoreCase(value.getName())) {
                return value;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getKeyword() {
        return keyword;
    }
}
