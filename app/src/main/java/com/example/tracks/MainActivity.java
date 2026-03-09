package com.example.tracks;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
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

import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private FirebaseServices fbs;
    private BottomNavigationView bottomNavigationView;
    private ListFragmentType listType;
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
        //fbs.getAuth().signOut();
        listType = ListFragmentType.Regular;
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.action_home) {
                    //selectedFragment = new TrackListFragment();
                    selectedFragment = new TrackListFragment();
                } else if (item.getItemId() == R.id.action_add) {
                    selectedFragment = new AddTrackFragment();
                }
           else if (item.getItemId() == R.id.action_profile) {
                selectedFragment = new ProfileFragment();
            }

                     else if (item.getItemId() == R.id.action_trackmap) {
                        selectedFragment = new TrackListMap();
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
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.favcheck);// set drawable icon
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (fbs.getAuth().getCurrentUser() == null) {
            bottomNavigationView.setVisibility(View.GONE);
            gotoLoginFragment();
            pushFragment(new LoginFragment());
        } else {
            bottomNavigationView.setVisibility(View.VISIBLE);
            //fbs.getCurrentObjectUser();
            gotoTrackList();
            pushFragment(new TrackListMap());

        }
    } public User getUserDataObject()
    {
        return this.userData;
    }

    private void signout() {
        fbs.getAuth().signOut();
        bottomNavigationView.setVisibility(View.INVISIBLE);
        gotoLoginFragment();
    }

    private void gotoProfileFragment() {
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout,new ProfileFragment());
        ft.commit();
    }
    private void gotoLoginFragment() {
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout,new LoginFragment());
        ft.commit();
    }
    public void gotoTrackList() {
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout,new TrackListMap());
        ft.commit();
    }

    public User getUserData()
    {
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
                                    if (fbs.getAuth().getCurrentUser() != null && (fbs.getAuth().getCurrentUser().getEmail().equals(user.getUsername()))) {
                                        //if (fbs.getAuth().getCurrentUser().getEmail().equals(user.getUsername())) {
                                        currentUser[0] = document.toObject(User.class);
                                        fbs.setCurrentUser(currentUser[0]);
                                    }
                                }
                            } else {
                                Log.e("AllRestActivity: readData()", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "error reading!" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return currentUser[0];
    }
    public void pushFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }
}