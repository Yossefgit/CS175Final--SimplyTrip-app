package com.example.simplytrip;

public class TripActivityItem {

    private String title;
    private String time;
    private String notes;

    public TripActivityItem(String title, String time, String notes) {
        this.title = title;
        this.time = time;
        this.notes = notes;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getNotes() {
        return notes;
    }
}