package com.github.sofaid.app.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

/**
 * Created by robik on 12/22/16.
 */
public class GsonUtil {
    public static final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private static Gson gson;

    public static Gson buildGson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new GsonUTCDateAdapter())
                .setDateFormat(ISO_FORMAT)
                .create();
        return gson;
    }

    public static <T> T parse(String json, Class<T> classOfT) {
        if (gson == null) {
            gson = buildGson();
        }
        return gson.fromJson(json, classOfT);
    }
}
