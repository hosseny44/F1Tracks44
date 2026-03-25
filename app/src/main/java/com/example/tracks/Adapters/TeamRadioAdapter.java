package com.example.tracks.Adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracks.Classes.TeamRadio;
import com.example.tracks.R;

import java.util.List;

public class TeamRadioAdapter extends RecyclerView.Adapter<TeamRadioAdapter.RadioViewHolder> {

    private List<TeamRadio> radioList;
    private Context context;

    public TeamRadioAdapter(List<TeamRadio> radioList, Context context) {
        this.radioList = radioList;
        this.context = context;
    }

    @NonNull
    @Override
    public RadioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.radio_item, parent, false);
        return new RadioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RadioViewHolder holder, int position) {
        TeamRadio radio = radioList.get(position);

        holder.driverNumber.setText("Driver Number: " + radio.getDriver_number());
        holder.radioDate.setText("Date: " + radio.getDate());
        holder.tvSession.setText("Session: " + radio.getSession_key());
        holder.tvMeeting.setText("Meeting: " + radio.getMeeting_key());

        holder.playButton.setOnClickListener(v -> {
            MediaPlayer player = new MediaPlayer();
            try {
                player.setDataSource(radio.getRecording_url());
                player.prepare();
                player.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return radioList.size();
    }

    public static class RadioViewHolder extends RecyclerView.ViewHolder {
        TextView driverNumber, radioDate, tvSession, tvMeeting;
        Button playButton;

        public RadioViewHolder(@NonNull View itemView) {
            super(itemView);
            driverNumber = itemView.findViewById(R.id.driverNumber);
            radioDate = itemView.findViewById(R.id.radioDate);
            tvSession = itemView.findViewById(R.id.tvSession);
            tvMeeting = itemView.findViewById(R.id.tvMeeting);
            playButton = itemView.findViewById(R.id.playButton);
        }
    }
}