package com.example.simplytrip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TripDetailsActivity extends AppCompatActivity {

    private static final String MAPS_API_KEY = "AIzaSyCby6cjqCqt8pmLALe0GE87FFw77J4Oxdw";

    private ImageButton buttonBackDetails;
    private TextView textDestination;
    private TextView textLocation;
    private TextView textDates;
    private TextView textLength;
    private TextView textBudget;
    private TextView textTravelers;
    private TextView textPerPerson;
    private TextView textNotes;
    private LinearLayout mapCard;
    private WebView mapWebView;
    private RecyclerView recyclerActivities;
    private RecyclerView recyclerPacking;
    private Button buttonAddActivity;
    private Button buttonEditTrip;
    private Button buttonAddPackingItem;

    private Trip trip;
    private int tripIndex = -1;
    private ActivityAdapter activityAdapter;
    private PackingAdapter packingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        buttonBackDetails = findViewById(R.id.buttonBackDetails);
        textDestination = findViewById(R.id.textDestination);
        textLocation = findViewById(R.id.textLocation);
        textDates = findViewById(R.id.textDates);
        textLength = findViewById(R.id.textLength);
        textBudget = findViewById(R.id.textBudget);
        textTravelers = findViewById(R.id.textTravelers);
        textPerPerson = findViewById(R.id.textPerPerson);
        textNotes = findViewById(R.id.textNotes);
        mapCard = findViewById(R.id.mapCard);
        mapWebView = findViewById(R.id.mapWebView);
        recyclerActivities = findViewById(R.id.recyclerActivities);
        recyclerPacking = findViewById(R.id.recyclerPacking);
        buttonAddActivity = findViewById(R.id.buttonAddActivity);
        buttonEditTrip = findViewById(R.id.buttonEditTrip);
        buttonAddPackingItem = findViewById(R.id.buttonAddPackingItem);

        tripIndex = getIntent().getIntExtra("trip_index", -1);
        if (!loadTrip()) {
            Toast.makeText(this, "Unable to load trip details", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerActivities.setLayoutManager(new LinearLayoutManager(this));
        activityAdapter = new ActivityAdapter(trip.getActivities(), tripIndex);
        recyclerActivities.setAdapter(activityAdapter);

        recyclerPacking.setLayoutManager(new LinearLayoutManager(this));
        packingAdapter = new PackingAdapter(trip.getPackingItems(), tripIndex);
        recyclerPacking.setAdapter(packingAdapter);

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

        buttonAddPackingItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPackingItemDialog();
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
        if (packingAdapter != null) {
            packingAdapter.notifyDataSetChanged();
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

        String location = trip.getDestination();

        if (location == null || location.trim().isEmpty()) {
            textLocation.setText("Location: not set");
            mapCard.setVisibility(View.GONE);
        } else {
            textLocation.setText(location);
            mapCard.setVisibility(View.VISIBLE);
            loadMapForLocation(location);
        }

        String dateText = trip.getStartDate() + " to " + trip.getEndDate();
        textDates.setText("Dates: " + dateText);

        String lengthText = buildLengthText(trip.getStartDate(), trip.getEndDate());
        textLength.setText(lengthText);

        String budget = trip.getBudget();
        if (budget == null || budget.isEmpty()) {
            textBudget.setText("Budget: not set");
        } else {
            textBudget.setText("Budget: " + budget);
        }

        String travelers = trip.getTravelers();
        if (travelers == null || travelers.isEmpty()) {
            textTravelers.setText("Travelers: not set");
        } else {
            textTravelers.setText("Travelers: " + travelers);
        }

        String perPersonText = buildPerPersonText(budget, travelers);
        textPerPerson.setText(perPersonText);

        String notes = trip.getNotes();
        if (notes == null || notes.isEmpty()) {
            textNotes.setText("No notes added.");
        } else {
            textNotes.setText(notes);
        }
    }

    private void loadMapForLocation(String location) {
        String encoded;
        try {
            encoded = URLEncoder.encode(location, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            encoded = location;
        }

        String src = "https://www.google.com/maps/embed/v1/place?key="
                + MAPS_API_KEY + "&q=" + encoded;

        String html = "<html><body style=\"margin:0;padding:0;\">" +
                "<iframe width=\"100%\" height=\"100%\" frameborder=\"0\" style=\"border:0;\" " +
                "src=\"" + src + "\" allowfullscreen></iframe>" +
                "</body></html>";

        WebSettings settings = mapWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mapWebView.setWebViewClient(new WebViewClient());
        mapWebView.loadDataWithBaseURL("https://www.google.com", html, "text/html", "UTF-8", null);
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

    private String buildPerPersonText(String budget, String travelers) {
        if (budget == null || budget.isEmpty() || travelers == null || travelers.isEmpty()) {
            return "Per person: n/a";
        }
        try {
            double total = Double.parseDouble(budget);
            int count = Integer.parseInt(travelers);
            if (count <= 0) return "Per person: n/a";
            double per = total / count;
            String value = String.format(Locale.getDefault(), "%.2f", per);
            return "Per person: " + value;
        } catch (NumberFormatException e) {
            return "Per person: n/a";
        }
    }

    private void showAddPackingItemDialog() {
        EditText input = new EditText(this);
        input.setHint("e.g. Passport");

        new AlertDialog.Builder(this)
                .setTitle("Add packing item")
                .setView(input)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = input.getText().toString().trim();
                    if (name.isEmpty()) {
                        Toast.makeText(this, "Please enter an item name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    trip.addPackingItem(new PackingItem(name, false));
                    TripRepository.getInstance().saveTrips();
                    if (packingAdapter != null) {
                        packingAdapter.notifyItemInserted(trip.getPackingItems().size() - 1);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}