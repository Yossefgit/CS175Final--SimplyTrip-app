package com.example.simplytrip;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private final List<Trip> trips;

    public TripAdapter(List<Trip> trips) {
        this.trips = trips;
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
        holder.textTripName.setText(trip.getName());

        String dates = trip.getStartDate() + " to " + trip.getEndDate();
        holder.textTripDates.setText(dates);

        String extra = "";
        boolean hasBudget = trip.getBudget() != null && !trip.getBudget().isEmpty();
        boolean hasTravelers = trip.getTravelers() != null && !trip.getTravelers().isEmpty();

        if (hasBudget && hasTravelers) {
            extra = "Budget: " + trip.getBudget() + " • Travelers: " + trip.getTravelers();
        } else if (hasBudget) {
            extra = "Budget: " + trip.getBudget();
        } else if (hasTravelers) {
            extra = "Travelers: " + trip.getTravelers();
        }

        if (extra.isEmpty()) {
            holder.textTripExtra.setVisibility(View.GONE);
        } else {
            holder.textTripExtra.setVisibility(View.VISIBLE);
            holder.textTripExtra.setText(extra);
        }
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    static class TripViewHolder extends RecyclerView.ViewHolder {

        TextView textTripName;
        TextView textTripDates;
        TextView textTripExtra;

        TripViewHolder(@NonNull View itemView) {
            super(itemView);
            textTripName = itemView.findViewById(R.id.textTripName);
            textTripDates = itemView.findViewById(R.id.textTripDates);
            textTripExtra = itemView.findViewById(R.id.textTripExtra);
        }
    }
}
