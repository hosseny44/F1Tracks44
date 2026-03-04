package com.example.tracks;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class AddTrackFragment extends Fragment {

    private EditText editTrackName, editRaceDistance, editNumberOfLaps, editFirstGrandPrix,
    editCircuitType ,editTrackDirection  , editTrackWidth, editTyreWear,
            editWeatherConditions, editelevation , editDrivingDifficulty ,editLocation
            ;


    private ImageView imgTrack;
    private Button btnAddTrack;

    private Uri selectedImageUri;

    private FirebaseFirestore db;
    private FirebaseStorage storage;

    private ActivityResultLauncher<Intent> pickImageLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_track, container, false);

        editTrackName = view.findViewById(R.id.editTrackName);
        editRaceDistance = view.findViewById(R.id.editRaceDistance);
        editNumberOfLaps = view.findViewById(R.id.editNumberOfLaps);
        editFirstGrandPrix = view.findViewById(R.id.editFirstGrandPrix);
        imgTrack = view.findViewById(R.id.trackImage);
        editCircuitType = view.findViewById(R.id.editCircuitType);
        editTrackDirection = view.findViewById(R.id.editTrackDirection);
        editTrackWidth = view.findViewById(R.id.editTrackWidth);
        editTyreWear = view.findViewById(R.id.editTyreWear);
        editWeatherConditions = view.findViewById(R.id.editWeatherConditions);
        editelevation = view.findViewById(R.id.editelevation);
        editDrivingDifficulty = view.findViewById(R.id.editDrivingDifficulty);
        editLocation= view.findViewById(R.id.editLocation);
        btnAddTrack = view.findViewById(R.id.btnAddTrack);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        Glide.with(this).load(selectedImageUri).into(imgTrack);
                    }
                });

        imgTrack.setOnClickListener(v -> openGallery());

        btnAddTrack.setOnClickListener(v -> saveTrack());

        return view;
    }

    private void openGallery() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    private void saveTrack() {

        String trackName = editTrackName.getText().toString().trim() ;
        String raceDistance = editRaceDistance.getText().toString().trim();
        String numberOfLaps = editNumberOfLaps.getText().toString().trim();
        String firstGrandPrix = editFirstGrandPrix.getText().toString().trim();
        String circuitType = editCircuitType.getText().toString().trim();
        String trackDirection = editTrackDirection.getText().toString().trim();
        String trackWidth = editTrackWidth.getText().toString().trim();
        String tyreWear = editTyreWear.getText().toString().trim();
        String weatherConditions = editWeatherConditions.getText().toString().trim();
        String elevation = editelevation.getText().toString().trim();
        String drivingDifficulty = editDrivingDifficulty.getText().toString().trim();
        String Location = editLocation.getText().toString().trim();

        if (trackName.isEmpty() || raceDistance.isEmpty()
                || numberOfLaps.isEmpty() || firstGrandPrix.isEmpty() || circuitType.isEmpty()
                || trackDirection.isEmpty() || trackWidth.isEmpty() || tyreWear.isEmpty() || weatherConditions.isEmpty()
                || elevation.isEmpty() || drivingDifficulty.isEmpty() || Location.isEmpty())
        {

            Toast.makeText(getActivity(),
                    "Please fill all fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageUri == null) {
            Toast.makeText(getActivity(),
                    "Please choose an image",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        btnAddTrack.setEnabled(false);

        // رفع الصورة
        StorageReference imageRef = storage.getReference(
                "tracks/" + UUID.randomUUID().toString() + ".jpg");

        imageRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot ->
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {

                            String imageUrl = uri.toString();

                            F1Track track = new F1Track(
                                    trackName,
                                    raceDistance,
                                    numberOfLaps,
                                    firstGrandPrix,
                                    imageUrl,
                                    circuitType,
                                    trackDirection,
                                    trackWidth,
                                    tyreWear,
                                    weatherConditions,
                                    elevation,
                                    drivingDifficulty,
                                    Location
                            ) {

                                @Override
                                public void writeToParcel(@NonNull Parcel dest, int flags) {

                                }
                            };

                            db.collection("Tracks")
                                    .add(track)
                                    .addOnSuccessListener(doc -> {
                                        Toast.makeText(getActivity(),
                                                "Track added successfully!",
                                                Toast.LENGTH_SHORT).show();
                                        gotoTrackList();
                                    })
                                    .addOnFailureListener(e -> {
                                        btnAddTrack.setEnabled(true);
                                        Toast.makeText(getActivity(),
                                                e.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    });

                        }))
                .addOnFailureListener(e -> {
                    btnAddTrack.setEnabled(true);
                    Toast.makeText(getActivity(),
                            "Image upload failed",
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void gotoTrackList() {
        FragmentTransaction ft =
                getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, new TrackListFragment());
        ft.commit();
    }
}


