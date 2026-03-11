package com.example.tracks;

import java.util.List;

public class SeasonsResponse {
    private List<Championship> championships;

    public List<Championship> getChampionships() { return championships; }

    public static class Championship {
        private String championshipId;
        private String championshipName;
        private String url;
        private int year;

        public String getChampionshipId() { return championshipId; }
        public String getChampionshipName() { return championshipName; }
        public String getUrl() { return url; }
        public int getYear() { return year; }
    }
}