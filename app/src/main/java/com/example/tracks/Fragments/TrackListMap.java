package com.example.tracks.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracks.Classes.F1Track;
import com.example.tracks.R;
import com.example.tracks.Utilites.MyAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class TrackListMap extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private ArrayList<F1Track> trackList , filteredList;
    private FirebaseFirestore db;
    private SearchView srchView;

    public TrackListMap() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_track_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        srchView = view.findViewById(R.id.srchTrack);
        trackList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new MyAdapter(getActivity(), trackList , "map") ;
        recyclerView.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        loadTracks();
        srchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                applyFilter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                applyFilter(newText);
                return true;
            }
        });

        return view;
    }

    private void loadTracks() {
        db.collection("Tracks")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    trackList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        F1Track track = doc.toObject(F1Track.class);
                        if (track != null) trackList.add(track);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(Throwable::printStackTrace);
    }

    private void applyFilter(String query) {
        filteredList.clear();

        if (query.trim().isEmpty()) {
            adapter = new MyAdapter(getActivity(), trackList, "map");
            recyclerView.setAdapter(adapter);
            return;
        }

        for (F1Track track : trackList) {
            if (track.getTrackName().toLowerCase().contains(query.toLowerCase()) ||
                    track.getRaceDistance().toLowerCase().contains(query.toLowerCase()) ||
                    track.getNumberOfLaps().toLowerCase().contains(query.toLowerCase()) ||
                    track.getFirstGrandPrix().toLowerCase().contains(query.toLowerCase())
            ) {
                filteredList.add(track);
            }
        }

        adapter = new MyAdapter(getActivity(), filteredList , "map");
        recyclerView.setAdapter(adapter);
    }
}