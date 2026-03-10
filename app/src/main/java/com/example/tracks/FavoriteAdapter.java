package com.example.tracks;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<F1Track> trackList;
    private String pageType;
    private FirebaseServices fbs;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public FavoriteAdapter(Context context, ArrayList<F1Track> trackList, String pageType) {
        this.context = context;
        this.trackList = trackList;
        this.pageType = pageType;
        this.fbs = FirebaseServices.getInstance();
    }

    @Override
    public MyViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
        android.view.View v = android.view.LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        F1Track track = trackList.get(position);

        holder.CountryName.setText(track.getCountryName());
        holder.exp.setText(track.getEXP());

        Glide.with(context)
                .load(track.getImageUrl() != null ? track.getImageUrl() : R.drawable.ic_launcher_foreground)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.trackImage);

        Glide.with(context)
                .load(track.getImgCountry() != null ? track.getImgCountry() : R.drawable.ic_launcher_foreground)
                .circleCrop()
                .into(holder.countryImage);

        holder.ivFavorite.setImageResource(track.isFavorite() ? R.drawable.ic_fav2_foreground : R.drawable.ic_fav1_foreground);

        holder.ivFavorite.setOnClickListener(v -> {
            User u = fbs.getCurrentUser();
            if (u == null) return;

            boolean isFav = u.getFavorites().contains(track.getCountryName());

            if (isFav) {
                u.getFavorites().remove(track.getCountryName());
                track.setFavorite(false);
            } else {
                u.getFavorites().add(track.getCountryName());
                track.setFavorite(true);
            }

            fbs.setCurrentUser(u);
            fbs.updateUser(u);

            notifyItemChanged(position);

            Toast.makeText(context, track.isFavorite() ? "Added to favorites" : "Removed from favorites",
                    Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public void updateList(ArrayList<F1Track> newList) {
        trackList.clear();
        if (newList != null) trackList.addAll(newList);
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView CountryName, exp;
        ImageView trackImage, countryImage, ivFavorite;

        public MyViewHolder(android.view.View itemView, OnItemClickListener listener) {
            super(itemView);
            CountryName = itemView.findViewById(R.id.tvCountryName);
            exp = itemView.findViewById(R.id.tvEXP);
            trackImage = itemView.findViewById(R.id.ivStadiumImage);
            countryImage = itemView.findViewById(R.id.ivCountry);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(getAdapterPosition());
            });
        }
    }
}