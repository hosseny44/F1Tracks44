package com.example.tracks;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface OpenF1Service {

    // Teams API
    @GET("api/current/teams")
    Call<TeamsResponse> getTeams();

    // Team Radio API
    @GET("team_radio")
    Call<List<TeamRadio>> getTeamRadio();
}