package com.example.tracks;

public class TeamRadio {

    private int driver_number;
    private String recording_url;
    private String date;
    private String session_key;
    private String meeting_key;

    public TeamRadio() {}

    public int getDriver_number() { return driver_number; }
    public void setDriver_number(int driver_number) { this.driver_number = driver_number; }

    public String getRecording_url() { return recording_url; }
    public void setRecording_url(String recording_url) { this.recording_url = recording_url; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getSession_key() { return session_key; }
    public void setSession_key(String session_key) { this.session_key = session_key; }

    public String getMeeting_key() { return meeting_key; }
    public void setMeeting_key(String meeting_key) { this.meeting_key = meeting_key; }
}