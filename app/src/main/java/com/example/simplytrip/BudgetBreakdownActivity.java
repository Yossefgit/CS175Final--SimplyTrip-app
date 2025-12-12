package com.example.simplytrip;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class BudgetBreakdownActivity extends AppCompatActivity {

    private TextView textTotalBudget;
    private TextView textAllocatedTotal;
    private TextView textRemaining;
    private EditText editLodging;
    private EditText editFood;
    private EditText editTransport;
    private EditText editActivities;
    private EditText editOther;
    private Button buttonSave;
    private Button buttonCancel;
    private ImageButton buttonBack;

    private Trip trip;
    private int tripIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_breakdown);

        textTotalBudget = findViewById(R.id.textTotalBudget);
        textAllocatedTotal = findViewById(R.id.textAllocatedTotal);
        textRemaining = findViewById(R.id.textRemaining);
        editLodging = findViewById(R.id.editLodgingBudget);
        editFood = findViewById(R.id.editFoodBudget);
        editTransport = findViewById(R.id.editTransportBudget);
        editActivities = findViewById(R.id.editActivitiesBudget);
        editOther = findViewById(R.id.editOtherBudget);
        buttonSave = findViewById(R.id.buttonSaveBudget);
        buttonCancel = findViewById(R.id.buttonCancelBudget);
        buttonBack = findViewById(R.id.buttonBackBudget);

        tripIndex = getIntent().getIntExtra("trip_index", -1);
        if (tripIndex < 0 || tripIndex >= TripRepository.getInstance().getTrips().size()) {
            Toast.makeText(this, "Unable to load budget", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        trip = TripRepository.getInstance().getTrips().get(tripIndex);

        textTotalBudget.setText("Total budget: " + trip.getBudget());

        editLodging.setText(trip.getLodgingBudget());
        editFood.setText(trip.getFoodBudget());
        editTransport.setText(trip.getTransportBudget());
        editActivities.setText(trip.getActivitiesBudget());
        editOther.setText(trip.getOtherBudget());

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                updateSummary();
            }
        };

        editLodging.addTextChangedListener(watcher);
        editFood.addTextChangedListener(watcher);
        editTransport.addTextChangedListener(watcher);
        editActivities.addTextChangedListener(watcher);
        editOther.addTextChangedListener(watcher);

        buttonSave.setOnClickListener(v -> {
            double lodging = safeParseDouble(editLodging.getText().toString().trim());
            double food = safeParseDouble(editFood.getText().toString().trim());
            double transport = safeParseDouble(editTransport.getText().toString().trim());
            double activities = safeParseDouble(editActivities.getText().toString().trim());
            double other = safeParseDouble(editOther.getText().toString().trim());

            if (lodging < 0 || food < 0 || transport < 0 || activities < 0 || other < 0) {
                Toast.makeText(this, "Amounts cannot be negative", Toast.LENGTH_SHORT).show();
                return;
            }

            trip.setLodgingBudget(editLodging.getText().toString().trim());
            trip.setFoodBudget(editFood.getText().toString().trim());
            trip.setTransportBudget(editTransport.getText().toString().trim());
            trip.setActivitiesBudget(editActivities.getText().toString().trim());
            trip.setOtherBudget(editOther.getText().toString().trim());
            TripRepository.getInstance().saveTrips();
            Toast.makeText(this, "Budget breakdown saved", Toast.LENGTH_SHORT).show();
            finish();
        });

        buttonCancel.setOnClickListener(v -> finish());
        buttonBack.setOnClickListener(v -> finish());

        updateSummary();
    }

    private void updateSummary() {
        double totalBudget = safeParseDouble(trip.getBudget());
        double lodging = safeParseDouble(editLodging.getText().toString().trim());
        double food = safeParseDouble(editFood.getText().toString().trim());
        double transport = safeParseDouble(editTransport.getText().toString().trim());
        double activities = safeParseDouble(editActivities.getText().toString().trim());
        double other = safeParseDouble(editOther.getText().toString().trim());

        double allocated = lodging + food + transport + activities + other;
        double remaining = totalBudget - allocated;

        String allocatedText = String.format(Locale.getDefault(), "Allocated: %.2f", allocated);
        String remainingText = String.format(Locale.getDefault(), "Remaining: %.2f", remaining);

        textAllocatedTotal.setText(allocatedText);
        textRemaining.setText(remainingText);
    }

    private double safeParseDouble(String s) {
        if (s == null || s.isEmpty()) return 0;
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}