package com.example.tracks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamRadioFragment extends Fragment {

    private RecyclerView recyclerView;
    private TeamRadioAdapter adapter;

    public TeamRadioFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_radio, container, false);

        // ربط RecyclerView
        recyclerView = view.findViewById(R.id.recyclerTeamRadio);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // جلب بيانات Team Radio
        fetchTeamRadio();

        return view;
    }

    private void fetchTeamRadio() {
        OpenF1Service service = RetrofitClient
                .getClient()
                .create(OpenF1Service.class);

        Call<List<TeamRadio>> call = service.getTeamRadio();

        call.enqueue(new Callback<List<TeamRadio>>() {
            @Override
            public void onResponse(Call<List<TeamRadio>> call, Response<List<TeamRadio>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TeamRadio> radios = response.body();
                    adapter = new TeamRadioAdapter(radios, getContext());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<TeamRadio>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}