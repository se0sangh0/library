package com.example.libraryapp.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.libraryapp.LibraryApplication;
import com.example.libraryapp.databinding.ActivityMainBinding;
import com.example.libraryapp.ui.auth.LoginActivity;
import com.example.libraryapp.viewmodel.AccountViewModel;
import com.example.libraryapp.viewmodel.LibraryViewModelFactory;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AccountViewModel accountViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        LibraryApplication application = (LibraryApplication) getApplication();
        LibraryViewModelFactory factory = new LibraryViewModelFactory(application.getRepository(), application.getSessionManager());
        accountViewModel = new ViewModelProvider(this, factory).get(AccountViewModel.class);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(binding.navHostFragment.getId());
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
        }

        accountViewModel.getCurrentUser().observe(this, user -> {
            if (user == null) {
                navigateToLogin();
            }
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
