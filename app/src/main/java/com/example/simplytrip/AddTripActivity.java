package com.example.simplytrip;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AddTripActivity extends AppCompatActivity {

    private EditText editTripName;
    private EditText editNotes;
    private EditText editBudget;
    private EditText editTravelers;
    private Button buttonSelectDates;
    private Button buttonSaveTrip;
    private Button buttonCancelTrip;
    private TextView textTripLength;
    private ImageButton buttonBackAddTrip;

    private long selectedStartDate = -1L;
    private long selectedEndDate = -1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        editTripName = findViewById(R.id.editTripName);
        editNotes = findViewById(R.id.editNotes);
        editBudget = findViewById(R.id.editBudget);
        editTravelers = findViewById(R.id.editTravelers);
        buttonSelectDates = findViewById(R.id.buttonSelectDates);
        buttonSaveTrip = findViewById(R.id.buttonSaveTrip);
        buttonCancelTrip = findViewById(R.id.buttonCancelTrip);
        textTripLength = findViewById(R.id.textTripLength);
        buttonBackAddTrip = findViewById(R.id.buttonBackAddTrip);

        buttonSelectDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateRangePicker();
            }
        });

        buttonSaveTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTrip();
            }
        });

        buttonCancelTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonBackAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void openDateRangePicker() {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder =
                MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select trip dates");

        MaterialDatePicker<Pair<Long, Long>> picker = builder.build();

        picker.addOnPositiveButtonClickListener(selection -> {
            if (selection == null) {
                return;
            }

            Long start = selection.first;
            Long end = selection.second;

            if (start == null || end == null) {
                return;
            }

            selectedStartDate = start;
            selectedEndDate = end;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String startText = sdf.format(new Date(selectedStartDate));
            String endText = sdf.format(new Date(selectedEndDate));
            String rangeText = startText + " to " + endText;
            buttonSelectDates.setText(rangeText);

            long diffMillis = selectedEndDate - selectedStartDate;
            long days = TimeUnit.MILLISECONDS.toDays(diffMillis) + 1;
            String lengthText = "Trip length: " + days + " days";
            textTripLength.setText(lengthText);
        });

        picker.show(getSupportFragmentManager(), "trip_date_range_picker");
    }

    private void saveTrip() {
        String name = editTripName.getText().toString().trim();
        String notes = editNotes.getText().toString().trim();
        String budget = editBudget.getText().toString().trim();
        String travelers = editTravelers.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter a trip name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedStartDate == -1L || selectedEndDate == -1L) {
            Toast.makeText(this, "Please select trip dates", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String startDateText = sdf.format(new Date(selectedStartDate));
        String endDateText = sdf.format(new Date(selectedEndDate));

        Trip trip = new Trip(name, startDateText, endDateText, notes, budget, travelers);
        TripRepository.getInstance().addTrip(trip);

        Toast.makeText(this, "Trip saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}
