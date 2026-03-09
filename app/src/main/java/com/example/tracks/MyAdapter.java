package com.example.tracks;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        F1Track track = trackList.get(position);
        holder.CountryName.setText(  track.getCountryName());
        holder.exp.setText(track.getEXP());

        if (track.getImageUrl() == null || track.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(R.drawable.ic_launcher_foreground)
                    .into(holder.trackImage);
        } else {
            Glide.with(context)
                    .load(track.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(holder.trackImage);
        }

        // تحميل صورة الدولة
        if (track.getImgCountry() == null || track.getImgCountry().isEmpty()) {
            Glide.with(context)
                    .load(R.drawable.ic_launcher_foreground)
                    .circleCrop()
                    .into(holder.countryImage);
        } else {
            Glide.with(context)
                    .load(track.getImgCountry())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(holder.countryImage);
        }

        // التعامل مع الضغط على العنصر
        holder.itemView.setOnClickListener(v -> {
            if(pageType.equals("list")) {
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
            } else if(pageType.equals("map")) {
                Bundle args = new Bundle();
                args.putString("address", track.getLocation1());
                TrackMapFragment mapFragment = new TrackMapFragment();
                mapFragment.setArguments(args);
                FragmentTransaction ft = ((MainActivity) context)
                        .getSupportFragmentManager()
                        .beginTransaction();
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView CountryName, exp;
        ImageView trackImage, countryImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            CountryName = itemView.findViewById(R.id.tvCountryName);
            exp = itemView.findViewById(R.id.tvEXP);
            trackImage = itemView.findViewById(R.id.ivStadiumImage);
            countryImage = itemView.findViewById(R.id.ivCountry);
        }
    }
}