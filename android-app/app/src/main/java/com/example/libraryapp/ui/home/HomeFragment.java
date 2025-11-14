package com.example.libraryapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.libraryapp.LibraryApplication;
import com.example.libraryapp.databinding.FragmentHomeBinding;
import com.example.libraryapp.viewmodel.HomeViewModel;
import com.example.libraryapp.viewmodel.LibraryViewModelFactory;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LibraryApplication application = (LibraryApplication) requireActivity().getApplication();
        LibraryViewModelFactory factory = new LibraryViewModelFactory(application.getRepository(), application.getSessionManager());
        viewModel = new ViewModelProvider(requireActivity(), factory).get(HomeViewModel.class);

        HomePagerAdapter adapter = new HomePagerAdapter(this);
        binding.homeViewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.homeTabLayout, binding.homeViewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("대출중");
            } else {
                tab.setText("예약중");
            }
        }).attach();

        viewModel.getLoans().observe(getViewLifecycleOwner(), loans ->
                binding.activeLoansCount.setText("대출중 도서: " + loans.size() + "권"));

        viewModel.getReservations().observe(getViewLifecycleOwner(), reservations ->
                binding.pendingReservationsCount.setText("예약 대기: " + reservations.size() + "건"));
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refresh();
    }
}
