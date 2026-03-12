package com.example.tracks.DataBase;

import java.util.List;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RetrofitClient {

    private static final String BASE_URL = "https://api.openf1.org/v1/";
    private static Retrofit retrofit = null;

    public interface TeamRadioService {
        @GET("team_radio")
        Call<List<TeamRadio>> getTeamRadio();
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static TeamRadioService getRadioService() {
        return getClient().create(TeamRadioService.class);
    }
}