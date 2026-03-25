package com.example.tracks.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.tracks.Classes.F1Track;
import com.example.tracks.Classes.Race;
import com.example.tracks.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class TrackDetailsFragment extends Fragment {

    private TextView tvTrackName, tvRaceDistance, tvNumberOfLaps, tvFirstGrandPrix,
            tvCircuitType, tvTrackDirection, tvTrackWidth, tvWeatherConditions, tvElevation,
            tvDrivingDifficulty, tvLocation;

    private ImageView tvTrackPhoto;
    private F1Track myTrack;

    private Button btnReminder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_track_details, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();

    }

    @SuppressLint("WrongViewCast")
    private void init() {
        tvTrackName = getView().findViewById(R.id.tvTrackName);
        tvRaceDistance = getView().findViewById(R.id.tvRaceDistance);
        tvNumberOfLaps = getView().findViewById(R.id.tvNumberOfLaps);
        tvFirstGrandPrix = getView().findViewById(R.id.tvFirstGrandPrix);
        tvCircuitType = getView().findViewById(R.id.tvCircuitType);
        tvTrackDirection = getView().findViewById(R.id.tvTrackDirection);
        tvTrackWidth = getView().findViewById(R.id.tvTrackWidth);
        tvWeatherConditions = getView().findViewById(R.id.tvWeatherConditions);
        tvElevation = getView().findViewById(R.id.tvElevation);
        tvDrivingDifficulty = getView().findViewById(R.id.tvDrivingDifficulty);
        tvLocation = getView().findViewById(R.id.tvLocation);
        tvTrackPhoto = getView().findViewById(R.id.ivTrackPhoto);
        btnReminder = getView().findViewById(R.id.btnR);

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
        tvWeatherConditions.setText("Weather Conditions: " + myTrack.getWeatherConditions());
        tvElevation.setText("Elevation Above The Sea: " + myTrack.getElevation() + " m");
        tvDrivingDifficulty.setText("Driving Difficulty: " + myTrack.getDrivingDifficulty());
        tvLocation.setText("Location: " + myTrack.getLocation1());
        tvTrackPhoto = getView().findViewById(R.id.ivTrackPhoto);

        Glide.with(requireContext())
                .load(myTrack.getImageUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(tvTrackPhoto);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, 100);
        }
        btnReminder.setOnClickListener(v -> addTrackRaceReminder());
    }
    private ArrayList<Race> getAllRacesForSeason() {
        ArrayList<Race> races = new ArrayList<>();
        races.add(new Race("Bahrain GB ", "Bahrain International Circuit", 2026,5,12,18,0));
        races.add(new Race("Abu Dhabi GP", "Albert Park Grand Prix Circuit", 2026,5,17,17,0));
        races.add(new Race(" Japan GP ", "Suzuka Circuit", 2026,3,29,14,0));
        races.add(new Race("Saudi Arabia GP ", "Jeddah Corniche Circuit", 2026,4,19,20,0));
        races.add(new Race(" United States GP ", "Miami International Autodrome Circuit", 2026,5,3,16,0));

        return races;
    }

    private void addTrackRaceReminder() {
        ArrayList<Race> allRaces = getAllRacesForSeason();
        Race raceForThisTrack = null;

        for(Race r : allRaces){
            if(r.trackName.equalsIgnoreCase(myTrack.getTrackName())){
                raceForThisTrack = r;
                break;
            }
        }

        if(raceForThisTrack != null){
            addRaceReminderToCalendar(raceForThisTrack);
        } else {
            Toast.makeText(getActivity(), "Race for this track not found!", Toast.LENGTH_SHORT).show();
        }
    }

    private long getPrimaryCalendarId() {
        long calendarId = -1;
        Cursor cursor = getActivity().getContentResolver().query(
                CalendarContract.Calendars.CONTENT_URI,
                new String[]{CalendarContract.Calendars._ID, CalendarContract.Calendars.CALENDAR_DISPLAY_NAME},
                CalendarContract.Calendars.VISIBLE + " = 1 AND " + CalendarContract.Calendars.IS_PRIMARY + " = 1",
                null,
                CalendarContract.Calendars._ID + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            calendarId = cursor.getLong(0);
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
            Cursor c2 = getActivity().getContentResolver().query(
                    CalendarContract.Calendars.CONTENT_URI,
                    new String[]{CalendarContract.Calendars._ID},
                    CalendarContract.Calendars.VISIBLE + " = 1",
                    null,
                    CalendarContract.Calendars._ID + " ASC");
            if (c2 != null && c2.moveToFirst()) {
                calendarId = c2.getLong(0);
                c2.close();
            }
        }
        return calendarId;
    }
    private void addRaceReminderToCalendar(Race race) {
        long calendarId = getPrimaryCalendarId();
        if(calendarId == -1){
            Toast.makeText(getActivity(), "No calendar found on device!", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(race.year, race.month-1, race.day, race.hour, race.minute);

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
        values.put(CalendarContract.Events.DTEND, beginTime.getTimeInMillis() + 60*60*1000); // ساعة
        values.put(CalendarContract.Events.TITLE, "Race: " + race.raceName);
        values.put(CalendarContract.Events.DESCRIPTION, "Don't forget to watch the race!");
        values.put(CalendarContract.Events.CALENDAR_ID, calendarId);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

        Uri uri = getActivity().getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
        if(uri != null){
            long eventID = Long.parseLong(uri.getLastPathSegment());
            ContentValues reminder = new ContentValues();
            reminder.put(CalendarContract.Reminders.MINUTES, 60);
            reminder.put(CalendarContract.Reminders.EVENT_ID, eventID);
            reminder.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            getActivity().getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, reminder);
            Toast.makeText(getActivity(), "Reminder added for " + race.raceName, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Failed to add reminder", Toast.LENGTH_SHORT).show();
        }
    }
}