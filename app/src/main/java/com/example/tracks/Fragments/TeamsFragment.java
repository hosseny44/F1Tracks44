package com.example.tracks.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracks.Classes.F1RetrofitClient;
import com.example.tracks.Classes.F1Team;
import com.example.tracks.R;
import com.example.tracks.Classes.TeamsResponse;
import com.example.tracks.Adapters.TeamsAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TeamsAdapter adapter;
    private List<F1Team> teams = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_teams, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerTeams);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TeamsAdapter(teams);
        recyclerView.setAdapter(adapter);

        F1RetrofitClient.getApiService().getTeams().enqueue(new Callback<TeamsResponse>() {
            @Override
            public void onResponse(Call<TeamsResponse> call, Response<TeamsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    teams.addAll(response.body().getTeams());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<TeamsResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load teams", Toast.LENGTH_SHORT).show();
            }
        });
    }
}