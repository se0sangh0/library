package com.example.libraryapp.ui.account;

import android.content.Intent;
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
import com.example.libraryapp.data.model.User;
import com.example.libraryapp.databinding.FragmentAccountBinding;
import com.example.libraryapp.ui.auth.LoginActivity;
import com.example.libraryapp.ui.common.AdminRequestAdapter;
import com.example.libraryapp.viewmodel.AccountViewModel;
import com.example.libraryapp.viewmodel.LibraryViewModelFactory;

public class AccountFragment extends Fragment implements AdminRequestAdapter.Listener {
    private FragmentAccountBinding binding;
    private AccountViewModel viewModel;
    private AdminRequestAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LibraryApplication application = (LibraryApplication) requireActivity().getApplication();
        LibraryViewModelFactory factory = new LibraryViewModelFactory(application.getRepository(), application.getSessionManager());
        viewModel = new ViewModelProvider(requireActivity(), factory).get(AccountViewModel.class);

        adapter = new AdminRequestAdapter(this);
        binding.adminRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.adminRequestsRecyclerView.setAdapter(adapter);

        binding.logoutButton.setOnClickListener(v -> {
            viewModel.logout();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        });

        viewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                binding.accountName.setText("로그인이 필요합니다.");
                binding.accountEmail.setText("");
                binding.accountRole.setText("");
            } else {
                binding.accountName.setText(user.getName());
                binding.accountEmail.setText(user.getEmail());
                binding.accountRole.setText(user.getRole() == User.Role.ADMIN ? "관리자" : "회원");
            }
        });

        viewModel.getAdminRequests().observe(getViewLifecycleOwner(), requests -> {
            boolean hasRequests = requests != null && !requests.isEmpty();
            binding.adminPanelTitle.setVisibility(hasRequests ? View.VISIBLE : View.GONE);
            binding.adminRequestsRecyclerView.setVisibility(hasRequests ? View.VISIBLE : View.GONE);
            adapter.submitList(requests);
        });
    }

    @Override
    public void onApprove(com.example.libraryapp.data.model.AdminRequest request) {
        viewModel.approve(request);
        Toast.makeText(requireContext(), "승인했습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReject(com.example.libraryapp.data.model.AdminRequest request) {
        viewModel.reject(request);
        Toast.makeText(requireContext(), "거절했습니다.", Toast.LENGTH_SHORT).show();
    }
}
