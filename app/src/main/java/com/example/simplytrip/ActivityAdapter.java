package com.example.simplytrip;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {

    private final List<TripActivityItem> activities;

    public ActivityAdapter(List<TripActivityItem> activities) {
        this.activities = activities;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_activity, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        TripActivityItem item = activities.get(position);

        holder.textActivityTitle.setText(item.getTitle());

        String time = item.getTime();
        if (time == null || time.isEmpty()) {
            holder.textActivityTime.setText("Time: not set");
        } else {
            holder.textActivityTime.setText("Time: " + time);
        }

        String notes = item.getNotes();
        if (notes == null || notes.isEmpty()) {
            holder.textActivityNotes.setText("No notes for this activity.");
        } else {
            holder.textActivityNotes.setText(notes);
        }
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder {

        TextView textActivityTitle;
        TextView textActivityTime;
        TextView textActivityNotes;

        ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            textActivityTitle = itemView.findViewById(R.id.textActivityTitle);
            textActivityTime = itemView.findViewById(R.id.textActivityTime);
            textActivityNotes = itemView.findViewById(R.id.textActivityNotes);
        }
    }
}