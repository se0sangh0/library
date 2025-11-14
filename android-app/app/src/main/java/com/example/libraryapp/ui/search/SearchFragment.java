package com.example.libraryapp.ui.search;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.libraryapp.LibraryApplication;
import com.example.libraryapp.R;
import com.example.libraryapp.data.model.Book;
import com.example.libraryapp.databinding.FragmentSearchBinding;
import com.example.libraryapp.ui.book.BookDetailBottomSheetDialog;
import com.example.libraryapp.ui.common.BookAdapter;
import com.example.libraryapp.viewmodel.LibraryViewModelFactory;
import com.example.libraryapp.viewmodel.SearchViewModel;

public class SearchFragment extends Fragment implements BookAdapter.Listener {
    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;
    private BookAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LibraryApplication application = (LibraryApplication) requireActivity().getApplication();
        LibraryViewModelFactory factory = new LibraryViewModelFactory(application.getRepository(), application.getSessionManager());
        viewModel = new ViewModelProvider(requireActivity(), factory).get(SearchViewModel.class);

        adapter = new BookAdapter(this);
        binding.searchRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.searchRecyclerView.setAdapter(adapter);

        viewModel.getBooks().observe(getViewLifecycleOwner(), books -> {
            adapter.submitList(books);
            boolean empty = books == null || books.isEmpty();
            binding.searchEmptyView.setVisibility(empty ? View.VISIBLE : View.GONE);
        });

        viewModel.getUserMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                viewModel.clearMessage();
            }
        });

        binding.searchEditText.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String query = v.getText() != null ? v.getText().toString() : "";
                viewModel.search(query.trim());
                return true;
            }
            return false;
        });
    }

    @Override
    public void onPrimaryAction(Book book) {
        viewModel.borrowBook(book);
    }

    @Override
    public void onSecondaryAction(Book book) {
        viewModel.reserveBook(book);
    }

    @Override
    public void onItemClicked(Book book) {
        Bundle args = new Bundle();
        args.putSerializable(BookDetailBottomSheetDialog.ARG_BOOK, book);
        NavHostFragment.findNavController(this).navigate(R.id.bookDetailBottomSheetDialog, args);
    }
}
