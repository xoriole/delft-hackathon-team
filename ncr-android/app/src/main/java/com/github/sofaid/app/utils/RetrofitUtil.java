package com.github.sofaid.app.utils;

import com.google.gson.Gson;

import com.github.sofaid.app.constants.API;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by robik on 12/22/16.
 */

public class RetrofitUtil {

    public static <T> T createRetrofit(Class<T> serviceClass) {
        return createRetrofit(API.BASE_URL, serviceClass);
    }

    public static <T> T createRetrofit(String endpoint, Class<T> serviceClass) {
        Gson gson = GsonUtil.buildGson();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endpoint)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return (T) retrofit.create(serviceClass);
    }
}
