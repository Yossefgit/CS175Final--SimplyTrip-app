package com.example.simplytrip;

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

public class PackingAdapter extends RecyclerView.Adapter<PackingAdapter.PackingViewHolder> {

    private final List<PackingItem> items;
    private final int tripIndex;

    public PackingAdapter(List<PackingItem> items, int tripIndex) {
        this.items = items;
        this.tripIndex = tripIndex;
    }

    @NonNull
    @Override
    public PackingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_packing, parent, false);
        return new PackingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackingViewHolder holder, int position) {
        PackingItem item = items.get(position);
        holder.textPackingName.setText(item.getName());

        holder.checkPacked.setOnCheckedChangeListener(null);
        holder.checkPacked.setChecked(item.isPacked());
        applyCompletedStyle(holder, item.isPacked());

        holder.checkPacked.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            Trip trip = TripRepository.getInstance().getTrips().get(tripIndex);
            PackingItem p = trip.getPackingItems().get(pos);
            p.setPacked(isChecked);
            TripRepository.getInstance().saveTrips();

            applyCompletedStyle(holder, isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void applyCompletedStyle(PackingViewHolder holder, boolean packed) {
        if (packed) {
            holder.textPackingName.setPaintFlags(
                    holder.textPackingName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );
            holder.textPackingName.setAlpha(0.6f);
        } else {
            holder.textPackingName.setPaintFlags(
                    holder.textPackingName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG
            );
            holder.textPackingName.setAlpha(1f);
        }
    }

    class PackingViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkPacked;
        TextView textPackingName;
        Button buttonDeletePacking;

        PackingViewHolder(@NonNull View itemView) {
            super(itemView);

            checkPacked = itemView.findViewById(R.id.checkPacked);
            textPackingName = itemView.findViewById(R.id.textPackingName);
            buttonDeletePacking = itemView.findViewById(R.id.buttonDeletePacking);

            buttonDeletePacking.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return;

                new AlertDialog.Builder(itemView.getContext())
                        .setTitle("Delete item")
                        .setMessage("Remove this packing item?")
                        .setPositiveButton("Delete", (d, w) -> {
                            Trip trip = TripRepository.getInstance().getTrips().get(tripIndex);
                            trip.removePackingItem(pos);
                            TripRepository.getInstance().saveTrips();
                            notifyItemRemoved(pos);
                            notifyItemRangeChanged(pos, items.size());
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
        }
    }
}