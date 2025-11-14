package com.example.libraryapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.libraryapp.LibraryApplication;
import com.example.libraryapp.data.model.Loan;
import com.example.libraryapp.databinding.FragmentRecyclerListBinding;
import com.example.libraryapp.ui.common.LoanAdapter;
import com.example.libraryapp.viewmodel.HomeViewModel;
import com.example.libraryapp.viewmodel.LibraryViewModelFactory;

public class LoanListFragment extends Fragment implements LoanAdapter.Listener {
    private FragmentRecyclerListBinding binding;
    private HomeViewModel viewModel;
    private LoanAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecyclerListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LibraryApplication application = (LibraryApplication) requireActivity().getApplication();
        LibraryViewModelFactory factory = new LibraryViewModelFactory(application.getRepository(), application.getSessionManager());
        viewModel = new ViewModelProvider(requireActivity(), factory).get(HomeViewModel.class);

        adapter = new LoanAdapter(this);
        binding.genericRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.genericRecyclerView.setAdapter(adapter);

        viewModel.getLoans().observe(getViewLifecycleOwner(), loans -> {
            adapter.submitList(loans);
            binding.emptyTextView.setVisibility(loans == null || loans.isEmpty() ? View.VISIBLE : View.GONE);
            binding.emptyTextView.setText("대출중인 도서가 없습니다.");
        });
    }

    @Override
    public void onReturnClicked(Loan loan) {
        viewModel.returnBook(loan);
        Toast.makeText(requireContext(), "반납 요청이 전송되었습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onExtendClicked(Loan loan) {
        Toast.makeText(requireContext(), "연장 요청이 접수되었습니다.", Toast.LENGTH_SHORT).show();
    }
}
