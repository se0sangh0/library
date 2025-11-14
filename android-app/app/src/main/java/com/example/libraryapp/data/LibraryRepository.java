package com.example.libraryapp.data;

import com.example.libraryapp.data.model.AdminRequest;
import com.example.libraryapp.data.model.Book;
import com.example.libraryapp.data.model.Loan;
import com.example.libraryapp.data.model.Reservation;
import com.example.libraryapp.data.model.User;

import java.util.List;
import java.util.Optional;

public interface LibraryRepository {
    Optional<User> authenticate(String email, String password);

    Optional<User> register(String name, String email, String password);

    List<Book> getBooks();

    List<Book> searchBooks(String query);

    List<Loan> getLoansForUser(User user);

    List<Reservation> getReservationsForUser(User user);

    List<AdminRequest> getAdminRequests();

    void approveRequest(AdminRequest request);

    void rejectRequest(AdminRequest request);

    Loan borrowBook(User user, Book book);

    Reservation reserveBook(User user, Book book);

    void returnBook(User user, Book book);

    void cancelReservation(User user, Reservation reservation);
}
