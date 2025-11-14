package com.example.libraryapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.libraryapp.LibraryApplication;
import com.example.libraryapp.databinding.ActivityRegisterBinding;
import com.example.libraryapp.ui.main.MainActivity;
import com.example.libraryapp.viewmodel.AuthViewModel;
import com.example.libraryapp.viewmodel.LibraryViewModelFactory;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LibraryApplication application = (LibraryApplication) getApplication();
        LibraryViewModelFactory factory = new LibraryViewModelFactory(application.getRepository(), application.getSessionManager());
        viewModel = new ViewModelProvider(this, factory).get(AuthViewModel.class);

        setupObservers();
        setupListeners();
    }

    private void setupObservers() {
        viewModel.getLoading().observe(this, isLoading -> binding.registerProgress.setVisibility(isLoading ? View.VISIBLE : View.GONE));
        viewModel.getError().observe(this, error -> {
            if (TextUtils.isEmpty(error)) {
                binding.registerErrorText.setVisibility(View.GONE);
            } else {
                binding.registerErrorText.setText(error);
                binding.registerErrorText.setVisibility(View.VISIBLE);
            }
        });
        viewModel.getAuthenticatedUser().observe(this, user -> {
            if (user != null) {
                Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                navigateToMain();
            }
        });
    }

    private void setupListeners() {
        binding.registerSubmitButton.setOnClickListener(v -> {
            String name = binding.nameEditText.getText() != null ? binding.nameEditText.getText().toString() : "";
            String email = binding.registerEmailEditText.getText() != null ? binding.registerEmailEditText.getText().toString() : "";
            String password = binding.registerPasswordEditText.getText() != null ? binding.registerPasswordEditText.getText().toString() : "";
            viewModel.register(name.trim(), email.trim(), password);
        });
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
