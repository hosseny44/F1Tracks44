package com.example.tracks;

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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeasonsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SeasonsAdapter adapter;
    private List<SeasonsResponse.Championship> seasons = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seasons, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerSeasons);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SeasonsAdapter(seasons);
        recyclerView.setAdapter(adapter);

        F1RetrofitClient.getApiService().getSeasons(100,0).enqueue(new Callback<SeasonsResponse>() {
            @Override
            public void onResponse(Call<SeasonsResponse> call, Response<SeasonsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    seasons.addAll(response.body().getChampionships());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SeasonsResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load seasons", Toast.LENGTH_SHORT).show();
            }
        });
    }
}