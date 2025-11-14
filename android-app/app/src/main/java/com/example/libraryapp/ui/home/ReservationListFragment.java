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
import com.example.libraryapp.data.model.Reservation;
import com.example.libraryapp.databinding.FragmentRecyclerListBinding;
import com.example.libraryapp.ui.common.ReservationAdapter;
import com.example.libraryapp.viewmodel.HomeViewModel;
import com.example.libraryapp.viewmodel.LibraryViewModelFactory;

public class ReservationListFragment extends Fragment implements ReservationAdapter.Listener {
    private FragmentRecyclerListBinding binding;
    private HomeViewModel viewModel;
    private ReservationAdapter adapter;

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

        adapter = new ReservationAdapter(this);
        binding.genericRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.genericRecyclerView.setAdapter(adapter);

        viewModel.getReservations().observe(getViewLifecycleOwner(), reservations -> {
            adapter.submitList(reservations);
            binding.emptyTextView.setVisibility(reservations == null || reservations.isEmpty() ? View.VISIBLE : View.GONE);
            binding.emptyTextView.setText("예약중인 도서가 없습니다.");
        });
    }

    @Override
    public void onCancelClicked(Reservation reservation) {
        viewModel.cancelReservation(reservation);
        Toast.makeText(requireContext(), "예약이 취소되었습니다.", Toast.LENGTH_SHORT).show();
    }
}
