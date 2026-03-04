package com.example.tracks;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracks.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.MyViewHolder> {

    Context context;
    ArrayList<TrackItem> trackList;
    private OnItemClickListener itemClickListener;
    private OnFavoriteClickListener favoriteClickListener;
    private FirebaseServices fbs;
    private String pageType;

    public TrackListAdapter(Context context, ArrayList<TrackItem> trackList) {
        this.context = context;
        this.trackList = trackList;
        this.fbs = FirebaseServices.getInstance();
        this.pageType = pageType;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position){
        TrackItem track = trackList.get(position);

        holder.trackName.setText("Track Name: " + track.getTrackName());
        holder.raceDistance.setText("Race Distance: " + track.getRaceDistance() + " Km");
        holder.numberOfLaps.setText("Number Of Laps: " + track.getNumberOfLaps());
        holder.firstGrandPrix.setText("First Grand Prix: " + track.getFirstGrandPrix());

        if (track.getImageUrl() == null || track.getImageUrl().isEmpty()) {
            Picasso.get().load(R.drawable.ic_launcher_foreground).into(holder.trackImage);
        } else {
            Picasso.get().load(track.getImageUrl()).into(holder.trackImage);
        }

        // حالة المفضلة
        if (fbs.getCurrentUser() != null && fbs.getCurrentUser().getFavorites().contains(track.getTrackName())) {
            holder.ivFavorite.setImageResource(R.drawable.ic_fav2_foreground);
        } else {
            holder.ivFavorite.setImageResource(R.drawable.ic_fav1_foreground);
        }

        // الضغط على العنصر لفتح التفاصيل
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null)
            {
                itemClickListener.onItemClick(position);
            }
            if(pageType.equals("list"))
            {
                Bundle args = new Bundle();
                args.putParcelable("track", track);
                TrackDetailsFragment td = new TrackDetailsFragment();
                td.setArguments(args);
                FragmentTransaction ft = ((MainActivity) context)
                        .getSupportFragmentManager()
                        .beginTransaction();
                ft.replace(R.id.frameLayout, td);
                ft.addToBackStack(null);
                ft.commit();

            }
        });

        // الضغط على أيقونة المفضلة
        holder.ivFavorite.setOnClickListener(v -> {
            if (fbs.getCurrentUser() != null) {
                if (fbs.getCurrentUser().getFavorites().contains(track.getTrackName())) {
                    fbs.getCurrentUser().getFavorites().remove(track.getTrackName());
                    holder.ivFavorite.setImageResource(R.drawable.ic_fav1_foreground);
                    Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();
                } else {
                    fbs.getCurrentUser().getFavorites().add(track.getTrackName());
                    holder.ivFavorite.setImageResource(R.drawable.ic_fav2_foreground);
                    Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show();
                }
                fbs.setUserChangeFlag(true); // لتحديث المفضلة لاحقاً عند onPause
                if (favoriteClickListener != null) favoriteClickListener.onFavoriteClick(track);
            }
        });
    }

    @Override
    public int getItemCount(){
        return trackList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView trackName, raceDistance, numberOfLaps, firstGrandPrix;
        ImageView trackImage, ivFavorite;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            trackName = itemView.findViewById(R.id.tvTrackName);
            raceDistance = itemView.findViewById(R.id.tvRaceDistance);
            numberOfLaps = itemView.findViewById(R.id.tvNumberOfLaps);
            firstGrandPrix = itemView.findViewById(R.id.tvFirstGrandPrix);
            trackImage = itemView.findViewById(R.id.ivStadiumImage);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(TrackItem track);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnFavoriteClickListener(OnFavoriteClickListener listener) {
        this.favoriteClickListener = listener;
    }

    public void updateList(ArrayList<TrackItem> newList) {
        this.trackList = newList;
        notifyDataSetChanged();
    }
}