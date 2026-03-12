package com.example.tracks;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SeasonsAdapter extends RecyclerView.Adapter<SeasonsAdapter.SeasonViewHolder> {

    private List<SeasonsResponse.Championship> seasons;

    public SeasonsAdapter(List<SeasonsResponse.Championship> seasons) {
        this.seasons = seasons;
    }

    @NonNull
    @Override
    public SeasonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.season_item, parent, false);
        return new SeasonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SeasonViewHolder holder, int position) {
        SeasonsResponse.Championship season = seasons.get(position);
        holder.name.setText(season.getChampionshipName());
        holder.year.setText("Year: " + season.getYear());
        holder.url.setText("Wiki: " +season.getUrl());
        holder.url.setOnClickListener(v -> {
            String url = season.getUrl();
            if (url != null && !url.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return seasons.size();
    }

    public static class SeasonViewHolder extends RecyclerView.ViewHolder {
        TextView name, year, url;

        public SeasonViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvChampionshipName);
            year = itemView.findViewById(R.id.tvYear);
            url = itemView.findViewById(R.id.tvUrl);
        }
    }
}