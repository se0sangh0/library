package com.example.libraryapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.libraryapp.LibraryApplication;
import com.example.libraryapp.databinding.ActivityLoginBinding;
import com.example.libraryapp.ui.main.MainActivity;
import com.example.libraryapp.viewmodel.AuthViewModel;
import com.example.libraryapp.viewmodel.LibraryViewModelFactory;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LibraryApplication application = (LibraryApplication) getApplication();
        LibraryViewModelFactory factory = new LibraryViewModelFactory(application.getRepository(), application.getSessionManager());
        viewModel = new ViewModelProvider(this, factory).get(AuthViewModel.class);

        setupObservers();
        setupListeners();

        viewModel.loadSession();
    }

    private void setupObservers() {
        viewModel.getLoading().observe(this, isLoading -> binding.loginProgress.setVisibility(isLoading ? View.VISIBLE : View.GONE));
        viewModel.getError().observe(this, error -> {
            if (TextUtils.isEmpty(error)) {
                binding.loginErrorText.setVisibility(View.GONE);
            } else {
                binding.loginErrorText.setText(error);
                binding.loginErrorText.setVisibility(View.VISIBLE);
            }
        });
        viewModel.getAuthenticatedUser().observe(this, user -> {
            if (user != null) {
                navigateToMain();
            }
        });
    }

    private void setupListeners() {
        binding.loginButton.setOnClickListener(v -> {
            String email = binding.emailEditText.getText() != null ? binding.emailEditText.getText().toString() : "";
            String password = binding.passwordEditText.getText() != null ? binding.passwordEditText.getText().toString() : "";
            viewModel.login(email.trim(), password);
        });

        binding.registerContainer.setOnClickListener(v -> navigateToRegister());
        binding.goToRegisterText.setOnClickListener(v -> navigateToRegister());
    }

    private void navigateToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
