package com.example.simplytrip;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    public interface OnTripClickListener {
        void onTripClick(int position);
    }

    private final List<Trip> trips;
    private final OnTripClickListener listener;

    public TripAdapter(List<Trip> trips, OnTripClickListener listener) {
        this.trips = trips;
        this.listener = listener;
        sortTrips();
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = trips.get(position);

        String title = trip.getName();
        if (trip.isPinned()) title = title + " 📌";

        holder.textTripName.setText(title);
        holder.textTripLocation.setText(trip.getDestination());
        holder.textTripDates.setText(trip.getStartDate() + " to " + trip.getEndDate());

        String extra = "";
        boolean hasBudget = trip.getBudget() != null && !trip.getBudget().isEmpty();
        boolean hasTravelers = trip.getTravelers() != null && !trip.getTravelers().isEmpty();

        if (hasBudget && hasTravelers) extra = "Budget: " + trip.getBudget() + " • Travelers: " + trip.getTravelers();
        else if (hasBudget) extra = "Budget: " + trip.getBudget();
        else if (hasTravelers) extra = "Travelers: " + trip.getTravelers();

        if (extra.isEmpty()) holder.textTripExtra.setVisibility(View.GONE);
        else {
            holder.textTripExtra.setVisibility(View.VISIBLE);
            holder.textTripExtra.setText(extra);
        }
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    private void sortTrips() {
        Collections.sort(trips, new Comparator<Trip>() {
            @Override
            public int compare(Trip t1, Trip t2) {
                if (t1.isPinned() && !t2.isPinned()) return -1;
                if (!t1.isPinned() && t2.isPinned()) return 1;
                return t1.getName().compareToIgnoreCase(t2.getName());
            }
        });
    }

    class TripViewHolder extends RecyclerView.ViewHolder {

        TextView textTripName;
        TextView textTripLocation;
        TextView textTripDates;
        TextView textTripExtra;
        Button buttonPin;
        Button buttonDelete;

        TripViewHolder(@NonNull View itemView) {
            super(itemView);

            textTripName = itemView.findViewById(R.id.textTripName);
            textTripLocation = itemView.findViewById(R.id.textTripLocation);
            textTripDates = itemView.findViewById(R.id.textTripDates);
            textTripExtra = itemView.findViewById(R.id.textTripExtra);
            buttonPin = itemView.findViewById(R.id.buttonPin);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) listener.onTripClick(pos);
            });

            buttonPin.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return;
                Trip trip = trips.get(pos);
                trip.setPinned(!trip.isPinned());
                sortTrips();
                TripRepository.getInstance().saveTrips();
                notifyDataSetChanged();
            });

            buttonDelete.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return;

                new AlertDialog.Builder(itemView.getContext())
                        .setTitle("Delete trip")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Delete", (d, w) -> {
                            TripRepository.getInstance().deleteTrip(pos);
                            notifyDataSetChanged();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
        }
    }
}