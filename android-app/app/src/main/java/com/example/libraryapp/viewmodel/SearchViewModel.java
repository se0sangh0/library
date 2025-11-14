package com.example.libraryapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.libraryapp.data.LibraryRepository;
import com.example.libraryapp.data.model.Book;
import com.example.libraryapp.data.SessionManager;
import com.example.libraryapp.data.model.User;

import java.util.Collections;
import java.util.List;

public class SearchViewModel extends ViewModel {
    private final LibraryRepository repository;
    private final SessionManager sessionManager;
    private final MutableLiveData<List<Book>> books = new MutableLiveData<>(Collections.emptyList());
    private final MutableLiveData<String> userMessage = new MutableLiveData<>();

    public SearchViewModel(LibraryRepository repository, SessionManager sessionManager) {
        this.repository = repository;
        this.sessionManager = sessionManager;
        books.setValue(repository.getBooks());
    }

    public LiveData<List<Book>> getBooks() {
        return books;
    }

    public LiveData<String> getUserMessage() {
        return userMessage;
    }

    public void clearMessage() {
        userMessage.setValue(null);
    }

    public void search(String query) {
        books.setValue(repository.searchBooks(query));
    }

    public void borrowBook(Book book) {
        User user = sessionManager.getCurrentUser();
        if (user == null) {
            userMessage.setValue("로그인이 필요합니다.");
            return;
        }
        repository.borrowBook(user, book);
        userMessage.setValue("대출이 완료되었습니다.");
        books.setValue(repository.getBooks());
    }

    public void reserveBook(Book book) {
        User user = sessionManager.getCurrentUser();
        if (user == null) {
            userMessage.setValue("로그인이 필요합니다.");
            return;
        }
        repository.reserveBook(user, book);
        userMessage.setValue("예약이 완료되었습니다.");
        books.setValue(repository.getBooks());
    }
}
