package com.example.libraryapp.ui.book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.libraryapp.LibraryApplication;
import com.example.libraryapp.data.model.Book;
import com.example.libraryapp.databinding.BottomSheetBookDetailBinding;
import com.example.libraryapp.viewmodel.LibraryViewModelFactory;
import com.example.libraryapp.viewmodel.SearchViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BookDetailBottomSheetDialog extends BottomSheetDialogFragment {
    public static final String ARG_BOOK = "arg_book";

    private BottomSheetBookDetailBinding binding;
    private Book book;
    private SearchViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetBookDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            Object arg = getArguments().getSerializable(ARG_BOOK);
            if (arg instanceof Book) {
                book = (Book) arg;
            }
        }
        if (book == null) {
            dismiss();
            return;
        }

        LibraryApplication application = (LibraryApplication) requireActivity().getApplication();
        LibraryViewModelFactory factory = new LibraryViewModelFactory(application.getRepository(), application.getSessionManager());
        viewModel = new ViewModelProvider(requireActivity(), factory).get(SearchViewModel.class);

        viewModel.getUserMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                viewModel.clearMessage();
            }
        });

        binding.detailTitle.setText(book.getTitle());
        binding.detailAuthor.setText(book.getAuthor());
        binding.detailDescription.setText(book.getDescription());
        switch (book.getStatus()) {
            case AVAILABLE:
                binding.detailStatus.setText("대출 가능");
                binding.detailPrimaryAction.setText("대출하기");
                binding.detailSecondaryAction.setText("예약하기");
                binding.detailPrimaryAction.setEnabled(true);
                binding.detailSecondaryAction.setEnabled(true);
                break;
            case RESERVED:
                binding.detailStatus.setText("예약 중");
                binding.detailPrimaryAction.setText("대출하기");
                binding.detailSecondaryAction.setText("예약 대기중");
                binding.detailPrimaryAction.setEnabled(false);
                binding.detailSecondaryAction.setEnabled(false);
                break;
            case BORROWED:
                binding.detailStatus.setText("대출 중");
                binding.detailPrimaryAction.setText("대출 중");
                binding.detailSecondaryAction.setText("예약하기");
                binding.detailPrimaryAction.setEnabled(false);
                binding.detailSecondaryAction.setEnabled(true);
                break;
        }

        binding.detailPrimaryAction.setOnClickListener(v -> {
            if (book.getStatus() == Book.Status.AVAILABLE) {
                viewModel.borrowBook(book);
                dismiss();
            }
        });

        binding.detailSecondaryAction.setOnClickListener(v -> {
            if (book.getStatus() == Book.Status.AVAILABLE || book.getStatus() == Book.Status.BORROWED) {
                viewModel.reserveBook(book);
                dismiss();
            }
        });
    }
}
