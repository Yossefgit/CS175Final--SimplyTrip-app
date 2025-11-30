package com.example.simplytrip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TripMenuActivity extends AppCompatActivity {

    private TextView textTripsTitle;
    private TextView textNoTrips;
    private ImageButton buttonBack;
    private Button buttonAddTrip;
    private RecyclerView recyclerTrips;
    private TripAdapter tripAdapter;
    private List<Trip> trips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_menu);

        textTripsTitle = findViewById(R.id.textTripsTitle);
        textNoTrips = findViewById(R.id.textNoTrips);
        buttonBack = findViewById(R.id.buttonBack);
        buttonAddTrip = findViewById(R.id.buttonAddTrip);
        recyclerTrips = findViewById(R.id.recyclerTrips);

        trips = TripRepository.getInstance().getTrips();

        tripAdapter = new TripAdapter(trips, new TripAdapter.OnTripClickListener() {
            @Override
            public void onTripClick(int position) {
                Intent intent = new Intent(TripMenuActivity.this, TripDetailsActivity.class);
                intent.putExtra("trip_index", position);
                startActivity(intent);
            }
        });

        recyclerTrips.setLayoutManager(new LinearLayoutManager(this));
        recyclerTrips.setAdapter(tripAdapter);

        buttonAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripMenuActivity.this, AddTripActivity.class);
                startActivity(intent);
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateEmptyState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tripAdapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (trips.isEmpty()) {
            textNoTrips.setVisibility(View.VISIBLE);
            recyclerTrips.setVisibility(View.GONE);
        } else {
            textNoTrips.setVisibility(View.GONE);
            recyclerTrips.setVisibility(View.VISIBLE);
        }
    }
}