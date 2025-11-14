package com.example.libraryapp.ui.common;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.data.model.Book;
import com.example.libraryapp.databinding.ItemBookBinding;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    public interface Listener {
        void onPrimaryAction(Book book);

        void onSecondaryAction(Book book);

        void onItemClicked(Book book);
    }

    private final Listener listener;
    private final List<Book> items = new ArrayList<>();

    public BookAdapter(Listener listener) {
        this.listener = listener;
    }

    public void submitList(List<Book> books) {
        items.clear();
        if (books != null) {
            items.addAll(books);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemBookBinding binding = ItemBookBinding.inflate(inflater, parent, false);
        return new BookViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        private final ItemBookBinding binding;

        BookViewHolder(ItemBookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Book book) {
            binding.bookTitle.setText(book.getTitle());
            binding.bookAuthor.setText(book.getAuthor());
            switch (book.getStatus()) {
                case AVAILABLE:
                    binding.bookStatus.setText("대출 가능");
                    break;
                case RESERVED:
                    binding.bookStatus.setText("예약 중");
                    break;
                case BORROWED:
                    binding.bookStatus.setText("대출 중");
                    break;
            }
            binding.bookPrimaryActionButton.setOnClickListener(v -> listener.onPrimaryAction(book));
            binding.bookSecondaryActionButton.setOnClickListener(v -> listener.onSecondaryAction(book));
            binding.getRoot().setOnClickListener(v -> listener.onItemClicked(book));
        }
    }
}
