package com.example.libraryapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.libraryapp.data.LibraryRepository;
import com.example.libraryapp.data.SessionManager;
import com.example.libraryapp.data.model.AdminRequest;
import com.example.libraryapp.data.model.User;

import java.util.Collections;
import java.util.List;

public class AccountViewModel extends ViewModel {
    private final LibraryRepository repository;
    private final SessionManager sessionManager;

    private final MutableLiveData<User> currentUser = new MutableLiveData<>();
    private final MutableLiveData<List<AdminRequest>> adminRequests = new MutableLiveData<>(Collections.emptyList());

    public AccountViewModel(LibraryRepository repository, SessionManager sessionManager) {
        this.repository = repository;
        this.sessionManager = sessionManager;
        loadUser();
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public LiveData<List<AdminRequest>> getAdminRequests() {
        return adminRequests;
    }

    public void loadUser() {
        User user = sessionManager.getCurrentUser();
        currentUser.setValue(user);
        if (user != null && user.getRole() == User.Role.ADMIN) {
            adminRequests.setValue(repository.getAdminRequests());
        } else {
            adminRequests.setValue(Collections.emptyList());
        }
    }

    public void logout() {
        sessionManager.clear();
        currentUser.setValue(null);
        adminRequests.setValue(Collections.emptyList());
    }

    public void approve(AdminRequest request) {
        repository.approveRequest(request);
        loadUser();
    }

    public void reject(AdminRequest request) {
        repository.rejectRequest(request);
        loadUser();
    }
}
