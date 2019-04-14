package com.github.sofaid.app.models.enums;

import com.github.sofaid.app.utils.StringUtil;

/**
 * Created by robik on 6/9/17.
 */
public enum ActivityToShow {
    SIGNUP(1, "Sign up"),
    SIGNUP_SEEDWORDS(2, "Sign up seed words"),
    HOME(3, "Home");
    private int id;
    private String name;

    ActivityToShow(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static ActivityToShow fromId(Integer id) {
        if (id == null) {
            return null;
        }
        ActivityToShow[] values = ActivityToShow.values();
        for (ActivityToShow value : values) {
            if (id.equals(value.getId())) {
                return value;
            }
        }
        return null;
    }

    public static ActivityToShow fromName(String name) {
        if (StringUtil.isEmpty(name)) {
            return null;
        }
        ActivityToShow[] values = ActivityToShow.values();
        for (ActivityToShow value : values) {
            if (name.equalsIgnoreCase(value.getName())) {
                return value;
            }
        }
        return null;
    }
}
