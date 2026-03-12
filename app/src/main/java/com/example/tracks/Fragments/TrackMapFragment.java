package com.example.tracks.Fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tracks.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class TrackMapFragment extends DialogFragment implements OnMapReadyCallback {

    private static final String ARG_ADDRESS = "address";
    private String address;
    public static TrackMapFragment newInstance(String address) {
        TrackMapFragment fragment = new TrackMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ADDRESS, address);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_track_map, container, false);

        if (getArguments() != null) {
            address = getArguments().getString(ARG_ADDRESS);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_container);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (address != null && !address.isEmpty()) {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocationName(address, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    LatLng location = new LatLng(addresses.get(0).getLatitude(),
                            addresses.get(0).getLongitude());
                    googleMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(address));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}