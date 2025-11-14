package com.example.libraryapp.ui.common;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.data.model.Loan;
import com.example.libraryapp.databinding.ItemLoanBinding;

import java.util.ArrayList;
import java.util.List;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.LoanViewHolder> {
    public interface Listener {
        void onReturnClicked(Loan loan);

        void onExtendClicked(Loan loan);
    }

    private final Listener listener;
    private final List<Loan> items = new ArrayList<>();

    public LoanAdapter(Listener listener) {
        this.listener = listener;
    }

    public void submitList(List<Loan> loans) {
        items.clear();
        if (loans != null) {
            items.addAll(loans);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LoanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemLoanBinding binding = ItemLoanBinding.inflate(inflater, parent, false);
        return new LoanViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class LoanViewHolder extends RecyclerView.ViewHolder {
        private final ItemLoanBinding binding;

        LoanViewHolder(ItemLoanBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Loan loan) {
            binding.loanBookTitle.setText(loan.getBook().getTitle());
            binding.loanDueDate.setText("반납 예정일: " + loan.getDueDate());
            binding.loanReturnButton.setOnClickListener(v -> listener.onReturnClicked(loan));
            binding.loanExtendButton.setOnClickListener(v -> listener.onExtendClicked(loan));
        }
    }
}
