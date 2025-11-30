package com.example.simplytrip;

import java.util.ArrayList;
import java.util.List;

public class Trip {

    private String name;
    private String startDate;
    private String endDate;
    private String notes;
    private String budget;
    private String travelers;
    private boolean pinned;
    private List<TripActivityItem> activities;

    public Trip(String name, String startDate, String endDate, String notes, String budget, String travelers) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
        this.budget = budget;
        this.travelers = travelers;
        this.pinned = false;
        this.activities = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getNotes() {
        return notes;
    }

    public String getBudget() {
        return budget;
    }

    public String getTravelers() {
        return travelers;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public List<TripActivityItem> getActivities() {
        return activities;
    }

    public void addActivity(TripActivityItem activityItem) {
        activities.add(activityItem);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public void setTravelers(String travelers) {
        this.travelers = travelers;
    }
}