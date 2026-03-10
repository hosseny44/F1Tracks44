package com.example.tracks;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private ImageView Profile;
    private TextView tvName, tvEmail;
    private AppCompatButton btnFav, btnSet;
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

        fbs.getCurrentObjectUser(user -> {

            if (user != null) {

                tvName.setText(user.getFirstName());
                tvEmail.setText(user.getUsername());

                if (user.getPhoto() != null && !user.getPhoto().isEmpty()) {
                    Picasso.get().load(user.getPhoto()).into(Profile);
                    fbs.setSelectedImageURL(Uri.parse(user.getPhoto()));
                }
            }
        });
    }

    private void gotoUpdateProfileFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, new UpdateProfile2());
        ft.commit();
    }

    private void gotoFavorite() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager()
                .beginTransaction();
        ft.replace(R.id.frameLayout, new FavFragment());
        ft.commit();
    }

}