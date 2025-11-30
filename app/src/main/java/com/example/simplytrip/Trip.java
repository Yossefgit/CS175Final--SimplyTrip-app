package com.example.simplytrip;

public class Trip {

    private String name;
    private String startDate;
    private String endDate;
    private String notes;
    private String budget;
    private String travelers;

    public Trip(String name, String startDate, String endDate, String notes, String budget, String travelers) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
        this.budget = budget;
        this.travelers = travelers;
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
}
