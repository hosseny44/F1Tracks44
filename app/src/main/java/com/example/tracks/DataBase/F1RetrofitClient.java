package com.example.tracks.DataBase;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.List;

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

    public static Call<TeamsResponse> getTeams() {
        return getApiService().getTeams();
    }

    public static Call<List<TeamRadio>> getTeamRadio() {
        return getApiService().getTeamRadio();
    }

    public static Call<SeasonsResponse> getSeasons(int limit, int offset) {
        return getApiService().getSeasons(limit, offset);
    }
}