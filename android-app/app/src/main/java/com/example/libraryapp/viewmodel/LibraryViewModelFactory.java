package com.example.libraryapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.libraryapp.data.LibraryRepository;
import com.example.libraryapp.data.SessionManager;

public class LibraryViewModelFactory implements ViewModelProvider.Factory {
    private final LibraryRepository repository;
    private final SessionManager sessionManager;

    public LibraryViewModelFactory(LibraryRepository repository, SessionManager sessionManager) {
        this.repository = repository;
        this.sessionManager = sessionManager;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AuthViewModel.class)) {
            return (T) new AuthViewModel(repository, sessionManager);
        } else if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(repository, sessionManager);
        } else if (modelClass.isAssignableFrom(SearchViewModel.class)) {
            return (T) new SearchViewModel(repository, sessionManager);
        } else if (modelClass.isAssignableFrom(AccountViewModel.class)) {
            return (T) new AccountViewModel(repository, sessionManager);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
