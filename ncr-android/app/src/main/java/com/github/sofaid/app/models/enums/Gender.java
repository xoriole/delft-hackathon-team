package com.github.sofaid.app.models.enums;

import com.github.sofaid.app.utils.StringUtil;

/**
 * Created by robik on 6/9/17.
 */
public enum Gender {
    MALE(1, "Male"),
    FEMALE(2, "Female"),
    OTHER(3, "Other"),
    UNSPECIFIED(4, "Unspecified");
    private int id;
    private String name;

    Gender(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Gender fromId(Integer id) {
        if (id == null) {
            return null;
        }
        Gender[] values = Gender.values();
        for (Gender value : values) {
            if (id.equals(value.getId())) {
                return value;
            }
        }
        return null;
    }

    public static Gender fromName(String name) {
        if (StringUtil.isEmpty(name)) {
            return null;
        }
        Gender[] values = Gender.values();
        for (Gender value : values) {
            if (name.equalsIgnoreCase(value.getName())) {
                return value;
            }
        }
        return null;
    }
}
