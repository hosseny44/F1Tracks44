package com.example.tracks.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.tracks.R;

public class AdminFragment extends Fragment {

    private Button btnPage1, btnPage2;

    public AdminFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin, container, false);

        btnPage1 = view.findViewById(R.id.btnPage1);
        btnPage2 = view.findViewById(R.id.btnPage2);

        //  Add Track
        btnPage1.setOnClickListener(v -> {
            FragmentTransaction ft =
                    requireActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, new AddTrackFragment());
            ft.addToBackStack(null);
            ft.commit();
        });

        // All Track
        btnPage2.setOnClickListener(v -> {
            FragmentTransaction ft =
                    requireActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, new TrackListFragment());
            ft.addToBackStack(null);
            ft.commit();
        });

        return view;
    }
}


