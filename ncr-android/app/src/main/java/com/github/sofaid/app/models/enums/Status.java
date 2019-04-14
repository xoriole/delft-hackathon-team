package com.github.sofaid.app.models.enums;

import com.github.sofaid.app.utils.StringUtil;

/**
 * Created by robik on 6/9/17.
 */
public enum Status {
    PENDING(1, "Pending", "is pending"),
    SUCCESS(2, "Success", "was successful!"),
    FAILED(3, "Failed", "failed :-(");
    private int id;
    private String name;
    private String clause;

    Status(int id, String name, String clause) {
        this.id = id;
        this.name = name;
        this.clause = clause;
    }

    public static Status fromId(Integer id) {
        if (id == null) {
            return null;
        }
        Status[] values = Status.values();
        for (Status value : values) {
            if (id.equals(value.getId())) {
                return value;
            }
        }
        return null;
    }

    public static Status fromName(String name) {
        if (StringUtil.isEmpty(name)) {
            return null;
        }
        Status[] values = Status.values();
        for (Status value : values) {
            if (name.equalsIgnoreCase(value.getName())) {
                return value;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getClause() {
        return clause;
    }
}
