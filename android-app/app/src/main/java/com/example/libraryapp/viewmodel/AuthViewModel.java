package com.example.libraryapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.libraryapp.data.LibraryRepository;
import com.example.libraryapp.data.SessionManager;
import com.example.libraryapp.data.model.User;

import java.util.Optional;

public class AuthViewModel extends ViewModel {
    private final LibraryRepository repository;
    private final SessionManager sessionManager;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>(null);
    private final MutableLiveData<User> authenticatedUser = new MutableLiveData<>();

    public AuthViewModel(LibraryRepository repository, SessionManager sessionManager) {
        this.repository = repository;
        this.sessionManager = sessionManager;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<User> getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void login(String email, String password) {
        loading.setValue(true);
        Optional<User> result = repository.authenticate(email, password);
        if (result.isPresent()) {
            sessionManager.setCurrentUser(result.get());
            authenticatedUser.setValue(result.get());
            error.setValue(null);
        } else {
            error.setValue("로그인에 실패했습니다. 입력 정보를 확인해주세요.");
        }
        loading.setValue(false);
    }

    public void register(String name, String email, String password) {
        loading.setValue(true);
        Optional<User> result = repository.register(name, email, password);
        if (result.isPresent()) {
            sessionManager.setCurrentUser(result.get());
            authenticatedUser.setValue(result.get());
            error.setValue(null);
        } else {
            error.setValue("회원가입에 실패했습니다. 이메일 중복 여부를 확인해주세요.");
        }
        loading.setValue(false);
    }

    public void loadSession() {
        User user = sessionManager.getCurrentUser();
        if (user != null) {
            authenticatedUser.setValue(user);
        }
    }
}
