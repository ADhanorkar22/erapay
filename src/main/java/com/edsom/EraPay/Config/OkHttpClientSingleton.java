package com.edsom.EraPay.Config;

import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class OkHttpClientSingleton {
    private static OkHttpClient instance;

    private OkHttpClientSingleton() {
        // Private constructor to prevent instantiation
    }

    public static OkHttpClient getInstance() {
        if (instance == null) {
            synchronized (OkHttpClientSingleton.class) {
                if (instance == null) {
                    instance = new OkHttpClient.Builder()
                            .connectTimeout(60000, TimeUnit.MILLISECONDS)
                            .writeTimeout(60000, TimeUnit.MILLISECONDS)
                            .readTimeout(60000, TimeUnit.MILLISECONDS)
                            .build();
                }
            }
        }
        return instance;
    }
}
