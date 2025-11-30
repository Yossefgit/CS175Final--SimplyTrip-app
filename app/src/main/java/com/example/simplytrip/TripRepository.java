package com.example.simplytrip;

import java.util.ArrayList;
import java.util.List;

public class TripRepository {

    private static TripRepository instance;

    private final List<Trip> trips = new ArrayList<>();

    private TripRepository() {
    }

    public static TripRepository getInstance() {
        if (instance == null) {
            instance = new TripRepository();
        }
        return instance;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void addTrip(Trip trip) {
        trips.add(trip);
    }
}
