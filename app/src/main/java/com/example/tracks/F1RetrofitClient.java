package com.example.tracks;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class F1RetrofitClient {

    private static final String BASE_URL = "https://f1api.dev/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static OpenF1Service getApiService() {
        return getClient().create(OpenF1Service.class);
    }
}