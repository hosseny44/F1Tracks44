package com.example.tracks;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class FavFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoriteAdapter adapter;
    private SearchView searchView;
    private ArrayList<F1Track> favoriteTracks = new ArrayList<>();
    private ArrayList<F1Track> filteredList = new ArrayList<>();
    private FirebaseServices fbs;

    public FavFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fav, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        recyclerView = getView().findViewById(R.id.recyclerFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        searchView = getView().findViewById(R.id.srchTrack);
        fbs = FirebaseServices.getInstance();

        loadFavoriteTracks();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
    }

    private void loadFavoriteTracks() {
        User currentUser = fbs.getCurrentUser();
        if (currentUser == null) return;

        FirebaseFirestore db = fbs.getFire();
        db.collection("Tracks")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        favoriteTracks.clear();

                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            F1Track track = doc.toObject(F1Track.class);

                            // تحقق بالمفضلة حسب CountryName أو EXP
                            if (track != null && currentUser.getFavorites().contains(track.getCountryName())) {
                                track.setFavorite(true);
                                favoriteTracks.add(track);
                            }
                        }

                        adapter = new FavoriteAdapter(getActivity(), favoriteTracks, "list");
                        recyclerView.setAdapter(adapter);

                        adapter.setOnItemClickListener(new FavoriteAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                F1Track selectedTrack = favoriteTracks.get(position);
                                Bundle args = new Bundle();
                                args.putParcelable("track", (Parcelable) selectedTrack);

                                TrackDetailsFragment detailsFragment = new TrackDetailsFragment();
                                detailsFragment.setArguments(args);

                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.frameLayout, detailsFragment);
                                ft.addToBackStack(null);
                                ft.commit();
                            }
                        });

                    } else {
                        Log.e("FavoriteFragment", "Error loading tracks", task.getException());
                    }
                });
    }

    private void applyFilter(String query) {
        filteredList.clear();
        if (query.trim().isEmpty()) {
            adapter.updateList(favoriteTracks);
            return;
        }

        for (F1Track track : favoriteTracks) {
            if (track.getCountryName().toLowerCase().contains(query.toLowerCase()) ||
                    track.getEXP().toLowerCase().contains(query.toLowerCase()) ||
                    track.getTrackName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(track);
            }
        }

        adapter.updateList(filteredList);
    }
}