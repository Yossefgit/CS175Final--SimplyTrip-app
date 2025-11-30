package com.example.simplytrip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TripDetailsActivity extends AppCompatActivity {

    private ImageButton buttonBackDetails;
    private TextView textDestination;
    private TextView textDates;
    private TextView textLength;
    private TextView textBudget;
    private TextView textTravelers;
    private TextView textNotes;
    private RecyclerView recyclerActivities;
    private Button buttonAddActivity;
    private Button buttonEditTrip;

    private Trip trip;
    private int tripIndex = -1;
    private ActivityAdapter activityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        buttonBackDetails = findViewById(R.id.buttonBackDetails);
        textDestination = findViewById(R.id.textDestination);
        textDates = findViewById(R.id.textDates);
        textLength = findViewById(R.id.textLength);
        textBudget = findViewById(R.id.textBudget);
        textTravelers = findViewById(R.id.textTravelers);
        textNotes = findViewById(R.id.textNotes);
        recyclerActivities = findViewById(R.id.recyclerActivities);
        buttonAddActivity = findViewById(R.id.buttonAddActivity);
        buttonEditTrip = findViewById(R.id.buttonEditTrip);

        tripIndex = getIntent().getIntExtra("trip_index", -1);
        if (!loadTrip()) {
            Toast.makeText(this, "Unable to load trip details", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerActivities.setLayoutManager(new LinearLayoutManager(this));
        activityAdapter = new ActivityAdapter(trip.getActivities());
        recyclerActivities.setAdapter(activityAdapter);

        buttonBackDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripDetailsActivity.this, AddActivityActivity.class);
                intent.putExtra("trip_index", tripIndex);
                startActivity(intent);
            }
        });

        buttonEditTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripDetailsActivity.this, AddTripActivity.class);
                intent.putExtra("trip_index", tripIndex);
                startActivity(intent);
            }
        });

        bindTripToViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!loadTrip()) {
            finish();
            return;
        }
        bindTripToViews();
        if (activityAdapter != null) {
            activityAdapter.notifyDataSetChanged();
        }
    }

    private boolean loadTrip() {
        if (tripIndex < 0 || tripIndex >= TripRepository.getInstance().getTrips().size()) {
            return false;
        }
        trip = TripRepository.getInstance().getTrips().get(tripIndex);
        return true;
    }

    private void bindTripToViews() {
        if (trip == null) return;

        textDestination.setText(trip.getName());

        String dateText = trip.getStartDate() + " to " + trip.getEndDate();
        textDates.setText("Dates: " + dateText);

        String lengthText = buildLengthText(trip.getStartDate(), trip.getEndDate());
        textLength.setText(lengthText);

        String budget = trip.getBudget();
        if (budget == null || budget.isEmpty()) textBudget.setText("Budget: not set");
        else textBudget.setText("Budget: " + budget);

        String travelers = trip.getTravelers();
        if (travelers == null || travelers.isEmpty()) textTravelers.setText("Travelers: not set");
        else textTravelers.setText("Travelers: " + travelers);

        String notes = trip.getNotes();
        if (notes == null || notes.isEmpty()) textNotes.setText("No notes added.");
        else textNotes.setText(notes);
    }

    private String buildLengthText(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            if (start == null || end == null) return "Trip length: unknown";
            long diffMillis = end.getTime() - start.getTime();
            long days = TimeUnit.MILLISECONDS.toDays(diffMillis) + 1;
            return "Trip length: " + days + " days";
        } catch (ParseException e) {
            return "Trip length: unknown";
        }
    }
}