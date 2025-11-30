package com.example.simplytrip;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddActivityActivity extends AppCompatActivity {

    private ImageButton buttonBackAddActivity;
    private EditText editActivityTitle;
    private EditText editActivityTime;
    private EditText editActivityNotes;
    private Button buttonCancelActivity;
    private Button buttonSaveActivity;

    private int tripIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activity);

        buttonBackAddActivity = findViewById(R.id.buttonBackAddActivity);
        editActivityTitle = findViewById(R.id.editActivityTitle);
        editActivityTime = findViewById(R.id.editActivityTime);
        editActivityNotes = findViewById(R.id.editActivityNotes);
        buttonCancelActivity = findViewById(R.id.buttonCancelActivity);
        buttonSaveActivity = findViewById(R.id.buttonSaveActivity);

        tripIndex = getIntent().getIntExtra("trip_index", -1);
        if (tripIndex < 0 || tripIndex >= TripRepository.getInstance().getTrips().size()) {
            finish();
            return;
        }

        buttonBackAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonCancelActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonSaveActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveActivity();
            }
        });
    }

    private void saveActivity() {
        String title = editActivityTitle.getText().toString().trim();
        String time = editActivityTime.getText().toString().trim();
        String notes = editActivityNotes.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Please enter an activity title", Toast.LENGTH_SHORT).show();
            return;
        }

        Trip trip = TripRepository.getInstance().getTrips().get(tripIndex);
        TripActivityItem item = new TripActivityItem(title, time, notes);
        trip.addActivity(item);
        TripRepository.getInstance().saveTrips();

        Toast.makeText(this, "Activity added", Toast.LENGTH_SHORT).show();
        finish();
    }
}