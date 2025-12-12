package com.example.simplytrip;

import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {

    private final List<TripActivityItem> activities;
    private final int tripIndex;

    public ActivityAdapter(List<TripActivityItem> activities, int tripIndex) {
        this.activities = activities;
        this.tripIndex = tripIndex;
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
        String duration = item.getDuration();

        if ((time == null || time.isEmpty()) && (duration == null || duration.isEmpty())) {
            holder.textActivityTime.setVisibility(View.GONE);
        } else {
            holder.textActivityTime.setVisibility(View.VISIBLE);
            if (time == null) time = "";
            if (duration == null) duration = "";
            if (!time.isEmpty() && !duration.isEmpty()) {
                holder.textActivityTime.setText(time + " • " + duration);
            } else if (!time.isEmpty()) {
                holder.textActivityTime.setText(time);
            } else {
                holder.textActivityTime.setText(duration);
            }
        }

        String notes = item.getNotes();
        if (notes == null || notes.isEmpty()) {
            holder.textActivityNotes.setVisibility(View.GONE);
        } else {
            holder.textActivityNotes.setVisibility(View.VISIBLE);
            holder.textActivityNotes.setText(notes);
        }

        holder.checkActivityDone.setOnCheckedChangeListener(null);
        holder.checkActivityDone.setChecked(item.isCompleted());
        applyCompletedStyle(holder, item.isCompleted());

        holder.checkActivityDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            toggleActivityCompleted(holder.getAdapterPosition(), isChecked);
        });

        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            TripActivityItem act = activities.get(pos);
            boolean newState = !act.isCompleted();

            holder.checkActivityDone.setChecked(newState);
            toggleActivityCompleted(pos, newState);
        });
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    private void toggleActivityCompleted(int position, boolean completed) {
        if (position == RecyclerView.NO_POSITION) return;

        Trip trip = TripRepository.getInstance().getTrips().get(tripIndex);
        TripActivityItem item = trip.getActivities().get(position);
        item.setCompleted(completed);
        TripRepository.getInstance().saveTrips();
        notifyItemChanged(position);
    }

    private void applyCompletedStyle(ActivityViewHolder holder, boolean completed) {
        if (completed) {
            holder.textActivityTitle.setPaintFlags(holder.textActivityTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.textActivityTitle.setAlpha(0.6f);
            holder.textActivityTime.setAlpha(0.6f);
            holder.textActivityNotes.setAlpha(0.6f);
        } else {
            holder.textActivityTitle.setPaintFlags(holder.textActivityTitle.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            holder.textActivityTitle.setAlpha(1f);
            holder.textActivityTime.setAlpha(1f);
            holder.textActivityNotes.setAlpha(1f);
        }
    }

    class ActivityViewHolder extends RecyclerView.ViewHolder {

        TextView textActivityTitle;
        TextView textActivityTime;
        TextView textActivityNotes;
        Button buttonEditActivity;
        Button buttonDeleteActivity;
        CheckBox checkActivityDone;

        ActivityViewHolder(@NonNull View itemView) {
            super(itemView);

            textActivityTitle = itemView.findViewById(R.id.textActivityTitle);
            textActivityTime = itemView.findViewById(R.id.textActivityTime);
            textActivityNotes = itemView.findViewById(R.id.textActivityNotes);
            buttonEditActivity = itemView.findViewById(R.id.buttonEditActivity);
            buttonDeleteActivity = itemView.findViewById(R.id.buttonDeleteActivity);
            checkActivityDone = itemView.findViewById(R.id.checkActivityDone);

            buttonEditActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos == RecyclerView.NO_POSITION) return;

                    Intent intent = new Intent(itemView.getContext(), AddActivityActivity.class);
                    intent.putExtra("trip_index", tripIndex);
                    intent.putExtra("activity_index", pos);
                    itemView.getContext().startActivity(intent);
                }
            });

            buttonDeleteActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos == RecyclerView.NO_POSITION) return;

                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle("Delete activity")
                            .setMessage("Are you sure you want to delete this activity?")
                            .setPositiveButton("Delete", (d, w) -> {
                                Trip trip = TripRepository.getInstance().getTrips().get(tripIndex);
                                trip.removeActivity(pos);
                                TripRepository.getInstance().saveTrips();
                                notifyItemRemoved(pos);
                                notifyItemRangeChanged(pos, activities.size());
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
            });
        }
    }
}