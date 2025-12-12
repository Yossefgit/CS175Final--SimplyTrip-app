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
    private String destination;
    private double latitude;
    private double longitude;
    private List<PackingItem> packingItems;
    private String lodgingBudget;
    private String foodBudget;
    private String transportBudget;
    private String activitiesBudget;
    private String otherBudget;

    public Trip(String name, String startDate, String endDate, String notes, String budget, String travelers, String destination, double latitude, double longitude) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
        this.budget = budget;
        this.travelers = travelers;
        this.pinned = false;
        this.activities = new ArrayList<>();
        this.destination = destination;
        this.latitude = latitude;
        this.longitude = longitude;
        this.packingItems = new ArrayList<>();
        this.lodgingBudget = "";
        this.foodBudget = "";
        this.transportBudget = "";
        this.activitiesBudget = "";
        this.otherBudget = "";
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

    public void removeActivity(int index) {
        activities.remove(index);
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

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<PackingItem> getPackingItems() {
        if (packingItems == null) {
            packingItems = new ArrayList<>();
        }
        return packingItems;
    }

    public void addPackingItem(PackingItem item) {
        getPackingItems().add(item);
    }

    public void removePackingItem(int index) {
        getPackingItems().remove(index);
    }

    public String getLodgingBudget() {
        return lodgingBudget;
    }

    public void setLodgingBudget(String lodgingBudget) {
        this.lodgingBudget = lodgingBudget;
    }

    public String getFoodBudget() {
        return foodBudget;
    }

    public void setFoodBudget(String foodBudget) {
        this.foodBudget = foodBudget;
    }

    public String getTransportBudget() {
        return transportBudget;
    }

    public void setTransportBudget(String transportBudget) {
        this.transportBudget = transportBudget;
    }

    public String getActivitiesBudget() {
        return activitiesBudget;
    }

    public void setActivitiesBudget(String activitiesBudget) {
        this.activitiesBudget = activitiesBudget;
    }

    public String getOtherBudget() {
        return otherBudget;
    }

    public void setOtherBudget(String otherBudget) {
        this.otherBudget = otherBudget;
    }
}