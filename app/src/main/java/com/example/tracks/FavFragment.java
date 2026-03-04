package com.example.tracks;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FavFragment extends Fragment {

    private RecyclerView recyclerView;
    private TrackListAdapter myAdapter;
    private SearchView srchView;

    private ArrayList<TrackItem> tracks;
    private ArrayList<TrackItem> filteredList;

    private FirebaseServices fbs;

    public FavFragment() {}

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {

        recyclerView = getView().findViewById(R.id.rvTracklist);
        srchView = getView().findViewById(R.id.srchViewfavoriteFragment);

        fbs = FirebaseServices.getInstance();

        tracks = new ArrayList<>();
        filteredList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        myAdapter = new TrackListAdapter(getActivity(), tracks );
        recyclerView.setAdapter(myAdapter);

        myAdapter.setOnItemClickListener(position -> {

            Bundle args = new Bundle();
            args.putParcelable("track", tracks.get(position));

            TrackDetailsFragment cd = new TrackDetailsFragment();
            cd.setArguments(args);

            FragmentTransaction ft = getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction();

            ft.replace(R.id.frameLayout, cd);
            ft.addToBackStack(null);
            ft.commit();

        });

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

        loadFavoriteTracks();
    }

    private void loadFavoriteTracks() {

        fbs.getFire().collection("tracks2")
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        tracks.clear();

                        User u = fbs.getCurrentUser();

                        for (QueryDocumentSnapshot document : task.getResult()) {

                            TrackItem track = document.toObject(TrackItem.class);

                            if (u != null && u.getFavorites().contains(track.getId())) {
                                tracks.add(track);
                            }
                        }

                        myAdapter.notifyDataSetChanged();

                    } else {

                        Log.e("FavFragment", "Error getting documents", task.getException());
                    }
                });
    }

    private void applyFilter(String query) {

        filteredList.clear();

        if (query.trim().isEmpty()) {
            myAdapter.updateList(tracks);
            return;
        }

        for (TrackItem track : tracks) {

            if (track.getTrackName().toLowerCase().contains(query.toLowerCase()) ||
                    track.getRaceDistance().toLowerCase().contains(query.toLowerCase()) ||
                    track.getNumberOfLaps().toLowerCase().contains(query.toLowerCase()) ||
                    track.getFirstGrandPrix().toLowerCase().contains(query.toLowerCase())) {

                filteredList.add(track);
            }
        }

        if (filteredList.isEmpty()) {
            showNoDataDialogue();
        }

        myAdapter.updateList(filteredList);
    }

    private void showNoDataDialogue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("No Results");
        builder.setMessage("Try again!");
        builder.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_fav, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();

        User u = ((MainActivity)getActivity()).getUserDataObject();

        if (u != null)
            fbs.updateUser(u);
    }
}