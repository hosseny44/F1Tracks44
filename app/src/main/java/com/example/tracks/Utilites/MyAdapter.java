package com.example.tracks.Utilites;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tracks.Classes.F1Track;
import com.example.tracks.Fragments.MainActivity;
import com.example.tracks.R;
import com.example.tracks.Fragments.TrackDetailsFragment;
import com.example.tracks.Fragments.TrackMapFragment;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<F1Track> trackList;
    private String pageType;

    public MyAdapter(Context context, ArrayList<F1Track> trackList, String pageType) {
        this.context = context;
        this.trackList = trackList;
        this.pageType = pageType;
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

        Glide.with(context)
                .load(track.getImageUrl() == null || track.getImageUrl().isEmpty() ?
                        R.drawable.ic_launcher_foreground : track.getImageUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.trackImage);
        Glide.with(context)
                .load(track.getImgCountry() == null || track.getImgCountry().isEmpty() ?
                        R.drawable.ic_launcher_foreground : track.getImgCountry())
                .circleCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.countryImage);
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
        ImageView trackImage, countryImage;

        public MyViewHolder(android.view.View itemView) {
            super(itemView);
            CountryName = itemView.findViewById(R.id.tvCountryName);
            exp = itemView.findViewById(R.id.tvEXP);
            trackImage = itemView.findViewById(R.id.ivStadiumImage);
            countryImage = itemView.findViewById(R.id.ivCountry);
        }
    }
}