package com.example.simplytrip;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TripRepository {

    private static TripRepository instance;

    private final String PREF_NAME = "simplytrip_storage";
    private final String KEY_TRIPS = "saved_trips";

    private final Gson gson = new Gson();
    private List<Trip> trips = new ArrayList<>();
    private Context context;

    private TripRepository() {}

    public static TripRepository getInstance() {
        if (instance == null) instance = new TripRepository();
        return instance;
    }

    public void init(Context ctx) {
        context = ctx.getApplicationContext();
        loadTrips();
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void addTrip(Trip trip) {
        trips.add(trip);
        saveTrips();
    }

    public void deleteTrip(int index) {
        trips.remove(index);
        saveTrips();
    }

    public void saveTrips() {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String json = gson.toJson(trips);
        editor.putString(KEY_TRIPS, json);
        editor.apply();
    }

    private void loadTrips() {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_TRIPS, null);
        if (json != null) {
            Type type = new TypeToken<List<Trip>>() {}.getType();
            trips = gson.fromJson(json, type);
        }
    }
}