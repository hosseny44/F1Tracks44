package com.example.tracks;

import static android.app.PendingIntent.getActivity;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    private FirebaseServices fbs;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout fragmentContainer;
    private User userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {

        fbs = FirebaseServices.getInstance();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.action_home) {
                    selectedFragment = new TrackListFragment();
                } else if (item.getItemId() == R.id.action_add) {
                    selectedFragment = new AddTrackFragment();
                } else if (item.getItemId() == R.id.action_profile) {
                    selectedFragment = new ProfileFragment();
                } else if (item.getItemId() == R.id.action_more) {
                    showMoreMenu();
                    return true;
                } else if (item.getItemId() == R.id.action_signout) {
                    FirebaseAuth.getInstance().signOut();
                    bottomNavigationView.setVisibility(View.GONE);
                    gotoLoginFragment();
                    return true;
                }

                if (selectedFragment != null) {
                    pushFragment(selectedFragment);
                }
                return true;
            }
        });

        fragmentContainer = findViewById(R.id.frameLayout);
        userData = getUserData();

        if (fbs.getAuth().getCurrentUser() == null) {
            bottomNavigationView.setVisibility(View.GONE);
            pushFragment(new LoginFragment());
        } else {
            bottomNavigationView.setVisibility(View.VISIBLE);

            // جلب بيانات المستخدم وضبط زر Add للأدمن فقط
            fbs.getCurrentObjectUser(user -> setNavigationBarVisible());

            pushFragment(new TrackListMap());
        }
    }

    private void showMoreMenu() {
        View moreButton = findViewById(R.id.action_more);
        PopupMenu popup = new PopupMenu(this, moreButton);
        popup.getMenu().add("Map");
        popup.getMenu().add("Races");
        popup.getMenu().add("Team Radio");

        popup.setOnMenuItemClickListener(menuItem -> {
            String title = menuItem.getTitle().toString();
            Fragment fragment = null;

            switch (title) {
                case "Map":
                    fragment = new TrackListMap();
                    break;
                case "Races":
                    fragment = new TeamsFragment();
                    break;
                case "Team Radio":
                    fragment = new TeamRadioFragment();
                    break;
            }

            if (fragment != null) pushFragment(fragment);
            return true;
        });

        popup.show();
    }

    public User getUserDataObject() {
        return this.userData;
    }

    private void gotoLoginFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, new LoginFragment());
        ft.commit();
    }

    public void pushFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    public User getUserData() {
        final User[] currentUser = {null};
        try {
            fbs.getFire().collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    User user = document.toObject(User.class);
                                    if (fbs.getAuth().getCurrentUser() != null &&
                                            fbs.getAuth().getCurrentUser().getEmail().equals(user.getUsername())) {
                                        currentUser[0] = document.toObject(User.class);
                                        fbs.setCurrentUser(currentUser[0]);
                                    }
                                }
                            } else {
                                Log.e("MainActivity: readData()", "Error getting documents.", task.getException());
                            }
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "error reading!" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return currentUser[0];
    }

    private void setNavigationBarVisible() {
        bottomNavigationView.setVisibility(View.VISIBLE);
        Menu menu = bottomNavigationView.getMenu();
        FirebaseUser currentUser = fbs.getAuth().getCurrentUser();

        if (currentUser == null || !currentUser.getEmail().equals("AA@gmail.com")) {
            menu.findItem(R.id.action_add).setVisible(false);
        } else {
            menu.findItem(R.id.action_add).setVisible(true);
        }
    }
}