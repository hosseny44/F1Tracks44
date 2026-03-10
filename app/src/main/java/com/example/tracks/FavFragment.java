package com.example.tracks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class FavFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private SearchView searchView;
    private TextView tvNoFavorites;
    private ArrayList<F1Track> trackList, filteredList;
    private FirebaseServices fbs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav, container, false);

        recyclerView = view.findViewById(R.id.rvFavoriteTracks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchView = view.findViewById(R.id.srchTrack);
        tvNoFavorites = view.findViewById(R.id.tvNoFavorites);

        fbs = FirebaseServices.getInstance();
        trackList = new ArrayList<>();
        filteredList = new ArrayList<>();

        myAdapter = new MyAdapter(getActivity(), trackList, "list");
        recyclerView.setAdapter(myAdapter);

        loadFavoriteTracks();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) { applyFilter(query); return true; }
            @Override public boolean onQueryTextChange(String newText) { applyFilter(newText); return true; }
        });

        return view;
    }

    private void loadFavoriteTracks() {
        trackList.clear();
        User user = fbs.getCurrentUser();
        if (user == null || user.getFavorites() == null || user.getFavorites().isEmpty()) {
            tvNoFavorites.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }

        fbs.getFire().collection("Tracks")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            F1Track track = doc.toObject(F1Track.class);
                            if (user.getFavorites().contains(track.getId())) {
                                track.setFavorite(true);
                                trackList.add(track);
                            }
                        }
                        tvNoFavorites.setVisibility(trackList.isEmpty() ? View.VISIBLE : View.GONE);
                        recyclerView.setVisibility(trackList.isEmpty() ? View.GONE : View.VISIBLE);
                        myAdapter.updateList(trackList);
                    }
                });
    }

    private void applyFilter(String query) {
        filteredList.clear();

        if (query.trim().isEmpty()) {
            myAdapter.updateList(trackList);
            return;
        }

        for (F1Track track : trackList) {
            if (track.getTrackName().toLowerCase().contains(query.toLowerCase()) ||
                    track.getCountryName().toLowerCase().contains(query.toLowerCase()) ||
                    track.getEXP().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(track);
            }
        }

        tvNoFavorites.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(filteredList.isEmpty() ? View.GONE : View.VISIBLE);
        myAdapter.updateList(filteredList);
    }
}