package com.example.tracks;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface OpenF1Service {

    @GET("team_radio")
    Call<List<TeamRadio>> getTeamRadio();

}