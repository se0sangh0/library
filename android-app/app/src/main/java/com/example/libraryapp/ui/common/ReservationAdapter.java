package com.example.libraryapp.ui.common;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.data.model.Reservation;
import com.example.libraryapp.databinding.ItemReservationBinding;

import java.util.ArrayList;
import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {
    public interface Listener {
        void onCancelClicked(Reservation reservation);
    }

    private final Listener listener;
    private final List<Reservation> items = new ArrayList<>();

    public ReservationAdapter(Listener listener) {
        this.listener = listener;
    }

    public void submitList(List<Reservation> reservations) {
        items.clear();
        if (reservations != null) {
            items.addAll(reservations);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemReservationBinding binding = ItemReservationBinding.inflate(inflater, parent, false);
        return new ReservationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ReservationViewHolder extends RecyclerView.ViewHolder {
        private final ItemReservationBinding binding;

        ReservationViewHolder(ItemReservationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Reservation reservation) {
            binding.reservationBookTitle.setText(reservation.getBook().getTitle());
            binding.reservationDate.setText("예약일: " + reservation.getRequestDate());
            binding.reservationCancelButton.setOnClickListener(v -> listener.onCancelClicked(reservation));
        }
    }
}
