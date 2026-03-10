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

        holder.CountryName.setText( track.getCountryName());
        holder.EXP.setText(  track.getRaceDistance());

        if (track.getImageUrl() == null || track.getImageUrl().isEmpty()) {
            Picasso.get().load(R.drawable.ic_launcher_foreground).into(holder.trackImage);
        } else {
            Picasso.get().load(track.getImageUrl()).into(holder.trackImage);
        }
        if (track.getImgCountry() == null || track.getImgCountry().isEmpty()) {
            Picasso.get().load(R.drawable.ic_launcher_foreground).into(holder.ivCountry);
        } else {
            Picasso.get().load(track.getImgCountry()).into(holder.ivCountry);
        }

        if (fbs.getCurrentUser() != null && fbs.getCurrentUser().getFavorites().contains(track.getId())) {
            holder.ivFavorite.setImageResource(R.drawable.ic_fav2_foreground);
        } else {
            holder.ivFavorite.setImageResource(R.drawable.ic_fav1_foreground);
        }
        // الضغط على أيقونة المفضلة
        holder.ivFavorite.setOnClickListener(v -> {
            v.setClickable(true); // تأكد أن الأيقونة قابلة للضغط
            v.setFocusable(true); // تجعلها قابلة للتفاعل
            if (fbs.getCurrentUser() != null) {
                boolean isFav = fbs.getCurrentUser().getFavorites().contains(track.getId());

                if (isFav) {
                    fbs.getCurrentUser().getFavorites().remove(track.getId());
                    holder.ivFavorite.setImageResource(R.drawable.ic_fav1_foreground);
                    Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();
                } else {
                    fbs.getCurrentUser().getFavorites().add(track.getId());
                    holder.ivFavorite.setImageResource(R.drawable.ic_fav2_foreground);
                    Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show();
                }

                // تحديث Firebase مباشرة
                fbs.updateUser(fbs.getCurrentUser());

                // إعلام FavFragment للتحديث
                if (favoriteClickListener != null) {
                    favoriteClickListener.onFavoriteClick(track);
                }
            }
        });
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null)
                itemClickListener.onItemClick(position);
        });


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

    }

    @Override
    public int getItemCount(){
        return trackList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView CountryName, EXP;
        ImageView trackImage, ivFavorite , ivCountry;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            CountryName = itemView.findViewById(R.id.tvCountryName);
            EXP = itemView.findViewById(R.id.tvEXP);
            trackImage = itemView.findViewById(R.id.ivStadiumImage);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
            ivCountry = itemView.findViewById(R.id.ivCountry);
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