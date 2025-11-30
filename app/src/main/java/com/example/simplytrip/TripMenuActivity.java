package com.example.simplytrip;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TripMenuActivity extends AppCompatActivity {

    private TextView textTripsTitle;
    private Button buttonAddTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_menu);

        textTripsTitle = findViewById(R.id.textTripsTitle);
        buttonAddTrip = findViewById(R.id.buttonAddTrip);

        buttonAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}