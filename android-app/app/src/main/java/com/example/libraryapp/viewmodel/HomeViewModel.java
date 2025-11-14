package com.example.libraryapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.libraryapp.data.LibraryRepository;
import com.example.libraryapp.data.SessionManager;
import com.example.libraryapp.data.model.Loan;
import com.example.libraryapp.data.model.Reservation;
import com.example.libraryapp.data.model.User;

import java.util.Collections;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private final LibraryRepository repository;
    private final SessionManager sessionManager;

    private final MutableLiveData<List<Loan>> loans = new MutableLiveData<>(Collections.emptyList());
    private final MutableLiveData<List<Reservation>> reservations = new MutableLiveData<>(Collections.emptyList());

    public HomeViewModel(LibraryRepository repository, SessionManager sessionManager) {
        this.repository = repository;
        this.sessionManager = sessionManager;
    }

    public LiveData<List<Loan>> getLoans() {
        return loans;
    }

    public LiveData<List<Reservation>> getReservations() {
        return reservations;
    }

    public void refresh() {
        User user = sessionManager.getCurrentUser();
        if (user == null) {
            loans.setValue(Collections.emptyList());
            reservations.setValue(Collections.emptyList());
            return;
        }
        loans.setValue(repository.getLoansForUser(user));
        reservations.setValue(repository.getReservationsForUser(user));
    }

    public void returnBook(Loan loan) {
        repository.returnBook(loan.getUser(), loan.getBook());
        refresh();
    }

    public void cancelReservation(Reservation reservation) {
        repository.cancelReservation(reservation.getUser(), reservation);
        refresh();
    }
}
