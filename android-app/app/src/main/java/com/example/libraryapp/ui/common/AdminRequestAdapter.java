package com.example.libraryapp.ui.common;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.data.model.AdminRequest;
import com.example.libraryapp.databinding.ItemAdminRequestBinding;

import java.util.ArrayList;
import java.util.List;

public class AdminRequestAdapter extends RecyclerView.Adapter<AdminRequestAdapter.AdminRequestViewHolder> {
    public interface Listener {
        void onApprove(AdminRequest request);

        void onReject(AdminRequest request);
    }

    private final Listener listener;
    private final List<AdminRequest> items = new ArrayList<>();

    public AdminRequestAdapter(Listener listener) {
        this.listener = listener;
    }

    public void submitList(List<AdminRequest> requests) {
        items.clear();
        if (requests != null) {
            items.addAll(requests);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdminRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemAdminRequestBinding binding = ItemAdminRequestBinding.inflate(inflater, parent, false);
        return new AdminRequestViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminRequestViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class AdminRequestViewHolder extends RecyclerView.ViewHolder {
        private final ItemAdminRequestBinding binding;

        AdminRequestViewHolder(ItemAdminRequestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(AdminRequest request) {
            binding.requestUserName.setText(request.getRequester().getName());
            binding.requestBookTitle.setText(request.getBook().getTitle());
            binding.approveButton.setOnClickListener(v -> listener.onApprove(request));
            binding.rejectButton.setOnClickListener(v -> listener.onReject(request));
        }
    }
}
