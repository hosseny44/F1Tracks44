package com.example.tracks;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

public class TrackDetailsFragment extends Fragment {

    private static final int PERMISSION_SEND_SMS = 1;
    private static final int REQUEST_CALL_PERMISSION = 2;

    private TextView tvTrackName, tvRaceDistance, tvNumberOfLaps, tvFirstGrandPrix,
            tvCircuitType, tvTrackDirection, tvTrackWidth,
            tvTyreWear, tvWeatherConditions, tvElevation,
            tvDrivingDifficulty, tvLocation;

    private ImageView ivTrackPhoto;
    private F1Track myTrack;

    private Button  btnSMS;
    private boolean isEnlarged = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_track_details, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();

        ivTrackPhoto.setOnClickListener(v -> {
            if (isEnlarged) {
                ivTrackPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else {
                ivTrackPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            isEnlarged = !isEnlarged;
        });
    }

    private void init() {

        tvTrackName = getView().findViewById(R.id.tvTrackName);
        tvRaceDistance = getView().findViewById(R.id.tvRaceDistance);
        tvNumberOfLaps = getView().findViewById(R.id.tvNumberOfLaps);
        tvFirstGrandPrix = getView().findViewById(R.id.tvFirstGrandPrix);
        tvCircuitType = getView().findViewById(R.id.tvCircuitType);
        tvTrackDirection = getView().findViewById(R.id.tvTrackDirection);
        tvTrackWidth = getView().findViewById(R.id.tvTrackWidth);
        tvTyreWear = getView().findViewById(R.id.tvTyreWear);
        tvWeatherConditions = getView().findViewById(R.id.tvWeatherConditions);
        tvElevation = getView().findViewById(R.id.tvElevation);
        tvDrivingDifficulty = getView().findViewById(R.id.tvDrivingDifficulty);
        tvLocation = getView().findViewById(R.id.tvLocation);
        ivTrackPhoto = getView().findViewById(R.id.ivTrackPhoto);

        Bundle args = getArguments();
        if (args == null || args.getParcelable("track") == null) {
            Toast.makeText(getActivity(), "Track data not found", Toast.LENGTH_SHORT).show();
            return;
        }

        myTrack = args.getParcelable("track");
        tvTrackName.setText("Track Name: " + myTrack.getTrackName());
        tvRaceDistance.setText("Race Distance: " + myTrack.getRaceDistance() + " Km");
        tvNumberOfLaps.setText("Number Of Laps: " + myTrack.getNumberOfLaps());
        tvFirstGrandPrix.setText("First Grand Prix: " + myTrack.getFirstGrandPrix());
        tvCircuitType.setText("Circuit Type: " + myTrack.getCircuitType());
        tvTrackDirection.setText("Track Direction: " + myTrack.getTrackDirection());
        tvTrackWidth.setText("Track Width: " + myTrack.getTrackWidth());
        tvTyreWear.setText("Tyre Wear: " + myTrack.getTyreWear());
        tvWeatherConditions.setText("Weather Conditions: " + myTrack.getWeatherConditions());
        tvElevation.setText("Elevation Above The Sea: " + myTrack.getElevation() + " m");
        tvDrivingDifficulty.setText("Driving Difficulty: " + myTrack.getDrivingDifficulty());
        tvLocation.setText("Location: " + myTrack.getLocation1());

        Picasso.get()
                .load(myTrack.getImageUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(ivTrackPhoto);

        btnSMS = getView().findViewById(R.id.btnSMS);
        btnSMS.setOnClickListener(v -> checkAndSendSMS());
    }

    private boolean isPhoneAvailable() {
        return myTrack != null && myTrack.getLocation1() != null && !myTrack.getLocation1().isEmpty();
    }

    // =================== SMS ===================
    private void checkAndSendSMS() {
        if (!isPhoneAvailable()) {
            Toast.makeText(getActivity(), "Phone number not available", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.SEND_SMS}, PERMISSION_SEND_SMS);
        } else {
            sendSMS();
        }
    }

    private void sendSMS() {
        String message = "I am interested in your " + myTrack.getTrackName() + " track";

        try {
            SmsManager.getDefault().sendTextMessage(
                    myTrack.getLocation1(), null, message, null, null);
            Toast.makeText(getActivity(), "SMS sent", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "SMS failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendWhatsAppMessage() {
        if (!isPhoneAvailable()) {
            Toast.makeText(getActivity(), "Phone number not available", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "https://wa.me/972" + myTrack.getLocation1()
                + "?text=" + Uri.encode("I am interested in your " + myTrack.getTrackName() + " track");

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        if (isAppInstalled("com.whatsapp")) {
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isAppInstalled(String packageName) {
        try {
            requireActivity().getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    // =================== Call ===================
    private void makePhoneCall() {
        if (!isPhoneAvailable()) {
            Toast.makeText(getActivity(), "Phone number not available", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            startCall();
        }
    }

    private void startCall() {
        Intent intent = new Intent(Intent.ACTION_CALL,
                Uri.parse("tel:" + myTrack.getLocation1()));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_SEND_SMS &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendSMS();
        }

        if (requestCode == REQUEST_CALL_PERMISSION &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCall();
        }
    }
}
