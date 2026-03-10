package com.example.tracks;

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

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class TrackListFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private ArrayList<F1Track> trackList , filteredList;
    private FirebaseFirestore db;
    private SearchView srchView;

    public TrackListFragment() { }

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

        adapter = new MyAdapter(getActivity(), trackList , "list") ;
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadTracks();

        // البحث
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
            adapter = new MyAdapter(getActivity(), trackList, "list");
            recyclerView.setAdapter(adapter);
            return;
        }

        for (F1Track track : trackList) {
            if (track.getCountryName().toLowerCase().contains(query.toLowerCase()) ||
                    track.getEXP().toLowerCase().contains(query.toLowerCase()));
            {
                filteredList.add(track);
            }
        }

        adapter = new MyAdapter(getActivity(), filteredList , "list");
        recyclerView.setAdapter(adapter);
    }
}