package com.example.tracks;

/**
 *
 */
public class Team {
    public String raceName;
    public String trackName; // اسم الحلبة
    public int year, month, day, hour, minute;

    public Team(String raceName, String trackName, int year, int month, int day, int hour, int minute) {
        this.raceName = raceName;
        this.trackName = trackName;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }
}