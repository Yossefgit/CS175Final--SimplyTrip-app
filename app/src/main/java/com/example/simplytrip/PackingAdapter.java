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
import java.util.Locale;

public class PackingAdapter extends RecyclerView.Adapter<PackingAdapter.PackingViewHolder> {

    public interface OnPackingChangedListener {
        void onPackingChanged();
    }

    private final List<PackingItem> items;
    private final int tripIndex;
    private final OnPackingChangedListener listener;

    public PackingAdapter(List<PackingItem> items, int tripIndex, OnPackingChangedListener listener) {
        this.items = items;
        this.tripIndex = tripIndex;
        this.listener = listener;
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

        String icon = getIconForCategory(item.getCategory());
        holder.textPackingIcon.setText(icon);

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
            if (listener != null) {
                listener.onPackingChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void applyCompletedStyle(PackingViewHolder holder, boolean packed) {
        if (packed) {
            holder.textPackingName.setPaintFlags(holder.textPackingName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.textPackingName.setAlpha(0.6f);
        } else {
            holder.textPackingName.setPaintFlags(holder.textPackingName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            holder.textPackingName.setAlpha(1f);
        }
    }

    private String getIconForCategory(String category) {
        if (category == null) return "🎒";
        String c = category.toLowerCase(Locale.getDefault());
        if (c.contains("lodging") || c.contains("clothes") || c.contains("clothing")) return "👕";
        if (c.contains("toiletries") || c.contains("tooth") || c.contains("soap") || c.contains("shampoo")) return "🧴";
        if (c.contains("electronics") || c.contains("charger") || c.contains("cable") || c.contains("laptop") || c.contains("phone")) return "🔌";
        if (c.contains("documents") || c.contains("passport") || c.contains("id") || c.contains("ticket")) return "📄";
        return "🎒";
    }

    class PackingViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkPacked;
        TextView textPackingName;
        TextView textPackingIcon;
        Button buttonDeletePacking;

        PackingViewHolder(@NonNull View itemView) {
            super(itemView);

            checkPacked = itemView.findViewById(R.id.checkPacked);
            textPackingName = itemView.findViewById(R.id.textPackingName);
            textPackingIcon = itemView.findViewById(R.id.textPackingIcon);
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
                            if (listener != null) {
                                listener.onPackingChanged();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
        }
    }
}