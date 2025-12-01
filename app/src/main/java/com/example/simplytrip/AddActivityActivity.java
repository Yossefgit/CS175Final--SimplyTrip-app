package com.example.simplytrip;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddActivityActivity extends AppCompatActivity {

    private ImageButton buttonBackAddActivity;
    private EditText editActivityTitle;
    private EditText editActivityNotes;
    private Button buttonCancelActivity;
    private Button buttonSaveActivity;
    private TextView textAddActivityTitle;
    private Button buttonSelectActivityTime;
    private TextView textActivityDuration;

    private int tripIndex = -1;
    private boolean isEditMode = false;
    private int activityIndex = -1;

    private String selectedTimeRange = "";
    private String selectedDurationText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activity);

        buttonBackAddActivity = findViewById(R.id.buttonBackAddActivity);
        editActivityTitle = findViewById(R.id.editActivityTitle);
        editActivityNotes = findViewById(R.id.editActivityNotes);
        buttonCancelActivity = findViewById(R.id.buttonCancelActivity);
        buttonSaveActivity = findViewById(R.id.buttonSaveActivity);
        textAddActivityTitle = findViewById(R.id.textAddActivityTitle);
        buttonSelectActivityTime = findViewById(R.id.buttonSelectActivityTime);
        textActivityDuration = findViewById(R.id.textActivityDuration);

        tripIndex = getIntent().getIntExtra("trip_index", -1);
        activityIndex = getIntent().getIntExtra("activity_index", -1);

        if (tripIndex < 0 || tripIndex >= TripRepository.getInstance().getTrips().size()) {
            finish();
            return;
        }

        if (activityIndex >= 0 && activityIndex < TripRepository.getInstance().getTrips().get(tripIndex).getActivities().size()) {
            isEditMode = true;
            TripActivityItem item = TripRepository.getInstance().getTrips().get(tripIndex).getActivities().get(activityIndex);
            prefillActivity(item);
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

        buttonSelectActivityTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeRangeDialog();
            }
        });
    }

    private void prefillActivity(TripActivityItem item) {
        textAddActivityTitle.setText("Edit Activity");
        buttonSaveActivity.setText("Save Changes");
        editActivityTitle.setText(item.getTitle());
        editActivityNotes.setText(item.getNotes());

        String time = item.getTime();
        if (time != null && !time.isEmpty()) {
            selectedTimeRange = time;
            buttonSelectActivityTime.setText(time);
        }

        String duration = item.getDuration();
        if (duration != null && !duration.isEmpty()) {
            selectedDurationText = duration;
            textActivityDuration.setText("Duration: " + duration);
        } else {
            textActivityDuration.setText("Duration: -");
        }
    }

    private void openTimeRangeDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_time_range, null);

        TimePicker timePickerStart = dialogView.findViewById(R.id.timePickerStart);
        TimePicker timePickerEnd = dialogView.findViewById(R.id.timePickerEnd);
        Button buttonCancelTime = dialogView.findViewById(R.id.buttonCancelTime);
        Button buttonOkTime = dialogView.findViewById(R.id.buttonOkTime);
        TextView textTotalDuration = dialogView.findViewById(R.id.textTotalDuration);

        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = roundToTen(now.get(Calendar.MINUTE));

        setTimeOnPicker(timePickerStart, hour, minute);
        setTimeOnPicker(timePickerEnd, hour, minute);

        updateDialogDuration(timePickerStart, timePickerEnd, textTotalDuration);

        TimePicker.OnTimeChangedListener listener = new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                updateDialogDuration(timePickerStart, timePickerEnd, textTotalDuration);
            }
        };

        timePickerStart.setOnTimeChangedListener(listener);
        timePickerEnd.setOnTimeChangedListener(listener);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        buttonCancelTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        buttonOkTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int startHour = getHourFromPicker(timePickerStart);
                int startMinute = roundToTen(getMinuteFromPicker(timePickerStart));
                int endHour = getHourFromPicker(timePickerEnd);
                int endMinute = roundToTen(getMinuteFromPicker(timePickerEnd));

                selectedTimeRange = formatTimeRange(startHour, startMinute, endHour, endMinute);
                selectedDurationText = formatDuration(startHour, startMinute, endHour, endMinute);

                buttonSelectActivityTime.setText(selectedTimeRange);
                if (selectedDurationText.isEmpty()) {
                    textActivityDuration.setText("Duration: -");
                } else {
                    textActivityDuration.setText("Duration: " + selectedDurationText);
                }

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void updateDialogDuration(TimePicker timePickerStart, TimePicker timePickerEnd, TextView textTotalDuration) {
        int startHour = getHourFromPicker(timePickerStart);
        int startMinute = roundToTen(getMinuteFromPicker(timePickerStart));
        int endHour = getHourFromPicker(timePickerEnd);
        int endMinute = roundToTen(getMinuteFromPicker(timePickerEnd));
        String duration = formatDuration(startHour, startMinute, endHour, endMinute);
        if (duration.isEmpty()) {
            textTotalDuration.setText("Total time: -");
        } else {
            textTotalDuration.setText("Total time: " + duration);
        }
    }

    private void setTimeOnPicker(TimePicker picker, int hour, int minute) {
        if (Build.VERSION.SDK_INT >= 23) {
            picker.setHour(hour);
            picker.setMinute(minute);
        } else {
            picker.setCurrentHour(hour);
            picker.setCurrentMinute(minute);
        }
    }

    private int getHourFromPicker(TimePicker picker) {
        if (Build.VERSION.SDK_INT >= 23) {
            return picker.getHour();
        } else {
            return picker.getCurrentHour();
        }
    }

    private int getMinuteFromPicker(TimePicker picker) {
        if (Build.VERSION.SDK_INT >= 23) {
            return picker.getMinute();
        } else {
            return picker.getCurrentMinute();
        }
    }

    private int roundToTen(int minute) {
        if (minute < 0) return 0;
        return (minute / 10) * 10;
    }

    private String formatTimeRange(int startHour, int startMinute, int endHour, int endMinute) {
        return formatTime(startHour, startMinute) + " – " + formatTime(endHour, endMinute);
    }

    private String formatTime(int hour, int minute) {
        int displayHour = hour % 12;
        if (displayHour == 0) displayHour = 12;
        String amPm = hour < 12 ? "AM" : "PM";
        String minuteStr = minute < 10 ? "0" + minute : String.valueOf(minute);
        return displayHour + ":" + minuteStr + " " + amPm;
    }

    private String formatDuration(int startHour, int startMinute, int endHour, int endMinute) {
        int startTotal = startHour * 60 + startMinute;
        int endTotal = endHour * 60 + endMinute;
        int diff = endTotal - startTotal;
        if (diff <= 0) {
            return "";
        }
        int hours = diff / 60;
        int minutes = diff % 60;
        if (hours > 0 && minutes > 0) {
            return hours + "h " + minutes + "m";
        } else if (hours > 0) {
            return hours + "h";
        } else {
            return minutes + "m";
        }
    }

    private void saveActivity() {
        String title = editActivityTitle.getText().toString().trim();
        String notes = editActivityNotes.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Please enter an activity title", Toast.LENGTH_SHORT).show();
            return;
        }

        Trip trip = TripRepository.getInstance().getTrips().get(tripIndex);
        String timeToSave = selectedTimeRange == null ? "" : selectedTimeRange;
        String durationToSave = selectedDurationText == null ? "" : selectedDurationText;

        if (isEditMode && activityIndex >= 0 && activityIndex < trip.getActivities().size()) {
            TripActivityItem item = trip.getActivities().get(activityIndex);
            item.setTitle(title);
            item.setTime(timeToSave);
            item.setNotes(notes);
            item.setDuration(durationToSave);
            TripRepository.getInstance().saveTrips();
            Toast.makeText(this, "Activity updated", Toast.LENGTH_SHORT).show();
        } else {
            TripActivityItem item = new TripActivityItem(title, timeToSave, notes, durationToSave);
            trip.addActivity(item);
            TripRepository.getInstance().saveTrips();
            Toast.makeText(this, "Activity added", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}