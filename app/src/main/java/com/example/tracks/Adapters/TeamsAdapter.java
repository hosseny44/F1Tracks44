package com.example.tracks.Adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracks.Classes.F1Team;
import com.example.tracks.R;

import java.util.List;

public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.TeamViewHolder> {

    private List<F1Team> teams;


    public TeamsAdapter(List<F1Team> teams) {
        this.teams = teams;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_item, parent, false);
        return new TeamViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        F1Team team = teams.get(position);
        holder.name.setText(team.getTeamName());
        holder.nationality.setText("Vationality:"+team.getTeamNationality());
        holder.firstAppearance.setText("First Appearance: " + team.getFirstAppeareance());
        holder.constructorsChampionships.setText("Constructors Championships: " + team.getConstructorsChampionships());
        holder.driversChampionships.setText("Drivers Championships: " + team.getDriversChampionships());
        holder.url.setText("Wiki: " + team.getUrl());;
        holder.url.setOnClickListener(v -> {
            String url = team.getUrl();
            if (url != null && !url.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    public static class TeamViewHolder extends RecyclerView.ViewHolder {
        TextView name, nationality, firstAppearance, constructorsChampionships, driversChampionships, url;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.teamName);
            nationality = itemView.findViewById(R.id.teamNationality);
            firstAppearance = itemView.findViewById(R.id.firstAppearance);
            constructorsChampionships = itemView.findViewById(R.id.constructorsChampionships);
            driversChampionships = itemView.findViewById(R.id.driversChampionships);
            url = itemView.findViewById(R.id.url);
        }
    }
}