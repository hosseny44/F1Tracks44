package com.example.tracks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private ImageView Profile;
    private TextView tvName, tvEmail;
    private AppCompatButton btnFav, btnCall, btnSet;
    private FirebaseServices fbs;

    public ProfileFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs = FirebaseServices.getInstance();

        Profile = getView().findViewById(R.id.Profile);
        tvName = getView().findViewById(R.id.tvName);
        tvEmail = getView().findViewById(R.id.tvEmail);
        btnFav = getView().findViewById(R.id.btnfav1);
        btnSet = getView().findViewById(R.id.btsSet);

        btnFav.setOnClickListener(v -> gotoFavorite());

        btnSet.setOnClickListener(v -> gotoUpdateProfileFragment());

        fillUserData();
    }

    private void fillUserData() {
        User current = fbs.getCurrentUser();
        if (current != null) {
            tvName.setText(current.getFirstName());
            tvEmail.setText(current.getAddress());

            if (current.getPhoto() != null && !current.getPhoto().isEmpty()) {
                Picasso.get().load(current.getPhoto()).into(Profile);
                fbs.setSelectedImageURL(Uri.parse(current.getPhoto()));
            }
        }
    }

    private void gotoUpdateProfileFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, new UpdateProfile2());
        ft.commit();
        ((MainActivity)getActivity()).getBottomNavigationView().setVisibility(View.VISIBLE);
    }
    private void gotoFavorite() {
        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout,new FavFragment());
        ft.commit();
    }
}