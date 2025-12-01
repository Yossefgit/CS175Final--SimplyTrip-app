package com.example.simplytrip;

import android.content.Intent;
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

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AddTripActivity extends AppCompatActivity {

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1001;
    private static final String MAPS_API_KEY = "AIzaSyCby6cjqCqt8pmLALe0GE87FFw77J4Oxdw";

    private EditText editTripName;
    private EditText editDestination;
    private EditText editNotes;
    private EditText editBudget;
    private EditText editTravelers;
    private Button buttonSelectDates;
    private Button buttonSaveTrip;
    private Button buttonCancelTrip;
    private TextView textTripLength;
    private ImageButton buttonBackAddTrip;
    private TextView textAddTripTitle;

    private long selectedStartDate = -1L;
    private long selectedEndDate = -1L;

    private boolean isEditMode = false;
    private int editTripIndex = -1;

    private String selectedDestinationName = "";
    private double selectedLat = 0;
    private double selectedLng = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        editTripName = findViewById(R.id.editTripName);
        editDestination = findViewById(R.id.editDestination);
        editNotes = findViewById(R.id.editNotes);
        editBudget = findViewById(R.id.editBudget);
        editTravelers = findViewById(R.id.editTravelers);
        buttonSelectDates = findViewById(R.id.buttonSelectDates);
        buttonSaveTrip = findViewById(R.id.buttonSaveTrip);
        buttonCancelTrip = findViewById(R.id.buttonCancelTrip);
        textTripLength = findViewById(R.id.textTripLength);
        buttonBackAddTrip = findViewById(R.id.buttonBackAddTrip);
        textAddTripTitle = findViewById(R.id.textAddTripTitle);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), MAPS_API_KEY);
        }

        editDestination.setFocusable(false);
        editDestination.setClickable(true);

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

        editDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlacesAutocomplete();
            }
        });

        if (getIntent() != null && getIntent().hasExtra("trip_index")) {
            int index = getIntent().getIntExtra("trip_index", -1);
            if (index >= 0 && index < TripRepository.getInstance().getTrips().size()) {
                isEditMode = true;
                editTripIndex = index;
                Trip trip = TripRepository.getInstance().getTrips().get(editTripIndex);
                prefillTrip(trip);
            }
        }
    }

    private void prefillTrip(Trip trip) {
        textAddTripTitle.setText("Edit Trip");
        buttonSaveTrip.setText("Save Changes");

        editTripName.setText(trip.getName());
        editNotes.setText(trip.getNotes());
        editBudget.setText(trip.getBudget());
        editTravelers.setText(trip.getTravelers());

        selectedDestinationName = trip.getDestination();
        selectedLat = trip.getLatitude();
        selectedLng = trip.getLongitude();
        editDestination.setText(selectedDestinationName);

        String startDate = trip.getStartDate();
        String endDate = trip.getEndDate();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            if (start != null && end != null) {
                selectedStartDate = start.getTime();
                selectedEndDate = end.getTime();
                buttonSelectDates.setText(startDate + " to " + endDate);
                long diffMillis = selectedEndDate - selectedStartDate;
                long days = TimeUnit.MILLISECONDS.toDays(diffMillis) + 1;
                textTripLength.setText("Trip length: " + days + " days");
            }
        } catch (ParseException e) {
        }
    }

    private void openPlacesAutocomplete() {
        List<Place.Field> fields = Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG
        );

        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                selectedDestinationName = place.getName();
                if (place.getLatLng() != null) {
                    selectedLat = place.getLatLng().latitude;
                    selectedLng = place.getLatLng().longitude;
                }
                editDestination.setText(selectedDestinationName);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR && data != null) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void openDateRangePicker() {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select trip dates");

        if (selectedStartDate > 0 && selectedEndDate > 0) {
            builder.setSelection(new Pair<>(selectedStartDate, selectedEndDate));
        }

        MaterialDatePicker<Pair<Long, Long>> picker = builder.build();

        picker.addOnPositiveButtonClickListener(selection -> {
            Long start = selection.first;
            Long end = selection.second;
            if (start == null || end == null) return;

            selectedStartDate = start;
            selectedEndDate = end;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String startText = sdf.format(new Date(selectedStartDate));
            String endText = sdf.format(new Date(selectedEndDate));
            buttonSelectDates.setText(startText + " to " + endText);

            long diffMillis = selectedEndDate - selectedStartDate;
            long days = TimeUnit.MILLISECONDS.toDays(diffMillis) + 1;
            textTripLength.setText("Trip length: " + days + " days");
        });

        picker.show(getSupportFragmentManager(), "trip_date_range_picker");
    }

    private void saveTrip() {
        String name = editTripName.getText().toString().trim();
        String notes = editNotes.getText().toString().trim();
        String budget = editBudget.getText().toString().trim();
        String travelers = editTravelers.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter a trip title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(selectedDestinationName)) {
            Toast.makeText(this, "Please select a destination", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedStartDate == -1L || selectedEndDate == -1L) {
            Toast.makeText(this, "Please select trip dates", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(budget)) {
            Toast.makeText(this, "Please enter a budget", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(travelers)) {
            Toast.makeText(this, "Please enter number of travelers", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String startDateText = sdf.format(new Date(selectedStartDate));
        String endDateText = sdf.format(new Date(selectedEndDate));

        if (isEditMode && editTripIndex >= 0 && editTripIndex < TripRepository.getInstance().getTrips().size()) {
            Trip trip = TripRepository.getInstance().getTrips().get(editTripIndex);
            trip.setName(name);
            trip.setStartDate(startDateText);
            trip.setEndDate(endDateText);
            trip.setNotes(notes);
            trip.setBudget(budget);
            trip.setTravelers(travelers);
            trip.setDestination(selectedDestinationName);
            trip.setLatitude(selectedLat);
            trip.setLongitude(selectedLng);
            TripRepository.getInstance().saveTrips();
            Toast.makeText(this, "Trip updated", Toast.LENGTH_SHORT).show();
        } else {
            Trip trip = new Trip(name, startDateText, endDateText, notes, budget, travelers, selectedDestinationName, selectedLat, selectedLng);
            TripRepository.getInstance().addTrip(trip);
            TripRepository.getInstance().saveTrips();
            Toast.makeText(this, "Trip saved", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}