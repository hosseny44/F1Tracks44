package com.example.tracks.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.tracks.Classes.User;
import com.example.tracks.FirebaseServices;
import com.example.tracks.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends AppCompatActivity {

    private FirebaseServices fbs;
    private BottomNavigationView bottomNavigationView;
    private User userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fbs = FirebaseServices.getInstance();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        init();
    }

    private void init() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.action_home) {
                selectedFragment = new TrackListFragment();
            } else if (itemId == R.id.action_add) {
                selectedFragment = new AddTrackFragment();
            } else if (itemId == R.id.action_profile) {
                selectedFragment = new ProfileFragment();
            } else if (itemId == R.id.action_more) {
                showMoreMenu();
                return true;
            } else if (itemId == R.id.action_signout) {
                FirebaseAuth.getInstance().signOut();
                updateBottomNavVisibility();
                pushFragment(new LoginFragment());
                return true;
            }

            if (selectedFragment != null) {
                pushFragment(selectedFragment);
            }
            return true;
        });

        updateBottomNavVisibility();

        if (fbs.getAuth().getCurrentUser() == null) {
            pushFragment(new LoginFragment());
        } else {
            pushFragment(new TrackListMap());
            fetchUserData();
        }
    }

    public void updateBottomNavVisibility() {
        if (fbs.getAuth().getCurrentUser() == null) {
            bottomNavigationView.setVisibility(View.GONE);
        } else {
            bottomNavigationView.setVisibility(View.VISIBLE);

            String adminEmail = "siraj@gmail.com";
            String currentUserEmail = fbs.getAuth().getCurrentUser().getEmail();

            Menu menu = bottomNavigationView.getMenu();
            MenuItem addItem = menu.findItem(R.id.action_add);

            if (addItem != null) {
                if (currentUserEmail != null && currentUserEmail.trim().equalsIgnoreCase(adminEmail)) {
                    addItem.setVisible(true);
                } else {
                    addItem.setVisible(false);
                }
            }
            invalidateOptionsMenu();
        }
    }

    public void pushFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }

    private void showMoreMenu() {
        View moreButton = findViewById(R.id.action_more);
        PopupMenu popup = new PopupMenu(this, moreButton);
        popup.getMenu().add("Map");
        popup.getMenu().add("All The Teams");
        popup.getMenu().add("Team Radio");
        popup.getMenu().add("All The Seasons");

        popup.setOnMenuItemClickListener(menuItem -> {
            String title = menuItem.getTitle().toString();
            Fragment fragment = null;
            switch (title) {
                case "Map": fragment = new TrackListMap(); break;
                case "All The Teams": fragment = new TeamsFragment(); break;
                case "Team Radio": fragment = new TeamRadioFragment(); break;
                case "All The Seasons": fragment = new SeasonsFragment(); break;
            }
            if (fragment != null) pushFragment(fragment);
            return true;
        });
        popup.show();
    }

    public void fetchUserData() {
        if (fbs.getAuth().getCurrentUser() == null) return;
        String email = fbs.getAuth().getCurrentUser().getEmail();
        fbs.getFire().collection("users")
                .whereEqualTo("username", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            userData = document.toObject(User.class);
                            fbs.setCurrentUser(userData);
                        }
                    }
                });
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }
}