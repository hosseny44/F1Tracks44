package com.example.tracks.Classes;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenF1Service {

    @GET("api/current/teams")
    Call<TeamsResponse> getTeams();

    // Team Radio API
    @GET("team_radio")
    Call<List<TeamRadio>> getTeamRadio();
    @GET("api/seasons")
    Call<SeasonsResponse> getSeasons(@Query("limit") int limit, @Query("offset") int offset);

}