package com.example.simplytrip;

public class TripActivityItem {

    private String title;
    private String time;
    private String notes;
    private String duration;

    public TripActivityItem(String title, String time, String notes, String duration) {
        this.title = title;
        this.time = time;
        this.notes = notes;
        this.duration = duration;
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

    public String getDuration() {
        return duration;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}