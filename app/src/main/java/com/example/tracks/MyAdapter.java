package com.example.tracks;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<F1Track> trackList;
    private String pageType;
    private FirebaseServices fbs;

    public MyAdapter(Context context, ArrayList<F1Track> trackList, String pageType) {
        this.context = context;
        this.trackList = trackList;
        this.pageType = pageType;
        this.fbs = FirebaseServices.getInstance();
    }


    @Override
    public MyViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
        android.view.View v = android.view.LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        F1Track track = trackList.get(position);

        holder.CountryName.setText(track.getCountryName());
        holder.exp.setText(track.getEXP());

        // تحميل صورة التراك
        Glide.with(context)
                .load(track.getImageUrl() == null || track.getImageUrl().isEmpty() ?
                        R.drawable.ic_launcher_foreground : track.getImageUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.trackImage);

        // تحميل صورة الدولة
        Glide.with(context)
                .load(track.getImgCountry() == null || track.getImgCountry().isEmpty() ?
                        R.drawable.ic_launcher_foreground : track.getImgCountry())
                .circleCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.countryImage);

        // --- Favorite logic ---
        holder.ivFavorite.setImageResource(track.isFavorite() ? R.drawable.star_cheak : R.drawable.star_24);

        holder.ivFavorite.setOnClickListener(v -> {
            User u = fbs.getCurrentUser();
            if (u == null) return;

            boolean isFav = u.getFavorites().contains(track.getId());

            if (isFav) {
                u.getFavorites().remove(track.getId());
                track.setFavorite(false);
            } else {
                u.getFavorites().add(track.getId());
                track.setFavorite(true);
            }

            // تحديث الذاكرة و Firebase فورًا
            fbs.setCurrentUser(u);
            fbs.updateUser(u);

            notifyItemChanged(position);

            Toast.makeText(context, track.isFavorite() ? "Added to favorites" : "Removed from favorites",
                    Toast.LENGTH_SHORT).show();
        });

        // الضغط على الصف نفسه
        holder.itemView.setOnClickListener(v -> {
            if (pageType.equals("list")) {
                Bundle args = new Bundle();
                args.putParcelable("track", track);
                TrackDetailsFragment td = new TrackDetailsFragment();
                td.setArguments(args);
                FragmentTransaction ft = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout, td);
                ft.addToBackStack(null);
                ft.commit();

            } else if (pageType.equals("map")) {
                Bundle args = new Bundle();
                args.putString("address", track.getLocation1());
                TrackMapFragment mapFragment = new TrackMapFragment();
                mapFragment.setArguments(args);
                FragmentTransaction ft = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout, mapFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public void updateList(ArrayList<F1Track> newList) {
        trackList.clear();
        if(newList != null) trackList.addAll(newList);
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView CountryName, exp;
        ImageView trackImage, countryImage, ivFavorite;

        public MyViewHolder(android.view.View itemView) {
            super(itemView);
            CountryName = itemView.findViewById(R.id.tvCountryName);
            exp = itemView.findViewById(R.id.tvEXP);
            trackImage = itemView.findViewById(R.id.ivStadiumImage);
            countryImage = itemView.findViewById(R.id.ivCountry);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);


        }
    }

}