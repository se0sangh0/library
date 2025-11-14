package com.example.libraryapp.data;

import android.text.TextUtils;

import com.example.libraryapp.data.model.AdminRequest;
import com.example.libraryapp.data.model.Book;
import com.example.libraryapp.data.model.Loan;
import com.example.libraryapp.data.model.Reservation;
import com.example.libraryapp.data.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class InMemoryLibraryRepository implements LibraryRepository {
    private final List<User> users = new ArrayList<>();
    private final List<Book> books = new ArrayList<>();
    private final List<Loan> loans = new ArrayList<>();
    private final List<Reservation> reservations = new ArrayList<>();
    private final List<AdminRequest> adminRequests = new ArrayList<>();

    public InMemoryLibraryRepository() {
        seedData();
    }

    private void seedData() {
        User admin = new User(UUID.randomUUID().toString(), "관리자", "admin@library.com", User.Role.ADMIN);
        User member = new User(UUID.randomUUID().toString(), "홍길동", "user@library.com", User.Role.MEMBER);
        users.add(admin);
        users.add(member);

        books.add(new Book("1", "클린 코드", "로버트 C. 마틴", "읽기 좋은 코드를 작성하는 방법을 설명합니다.", Book.Status.AVAILABLE));
        books.add(new Book("2", "리팩터링", "마틴 파울러", "소프트웨어 구조를 개선하는 기법을 다룹니다.", Book.Status.RESERVED));
        books.add(new Book("3", "Effective Java", "조슈아 블로크", "자바 개발자를 위한 베스트 프랙티스.", Book.Status.BORROWED));

        loans.add(new Loan(UUID.randomUUID().toString(), books.get(2), member, "2024-03-30"));
        reservations.add(new Reservation(UUID.randomUUID().toString(), books.get(1), member, "2024-03-15"));
        adminRequests.add(new AdminRequest(UUID.randomUUID().toString(), AdminRequest.Type.RESERVATION_APPROVAL, member, books.get(1)));
    }

    @Override
    public Optional<User> authenticate(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            return Optional.empty();
        }
        return users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public Optional<User> register(String name, String email, String password) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            return Optional.empty();
        }
        boolean exists = users.stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
        if (exists) {
            return Optional.empty();
        }
        User newUser = new User(UUID.randomUUID().toString(), name, email, User.Role.MEMBER);
        users.add(newUser);
        return Optional.of(newUser);
    }

    @Override
    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    @Override
    public List<Book> searchBooks(String query) {
        if (TextUtils.isEmpty(query)) {
            return getBooks();
        }
        String lowerQuery = query.toLowerCase();
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(lowerQuery)
                        || book.getAuthor().toLowerCase().contains(lowerQuery)
                        || book.getDescription().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    @Override
    public List<Loan> getLoansForUser(User user) {
        return loans.stream()
                .filter(loan -> loan.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> getReservationsForUser(User user) {
        return reservations.stream()
                .filter(reservation -> reservation.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<AdminRequest> getAdminRequests() {
        return new ArrayList<>(adminRequests);
    }

    @Override
    public void approveRequest(AdminRequest request) {
        adminRequests.removeIf(r -> r.getId().equals(request.getId()));
        request.getBook().setStatus(Book.Status.AVAILABLE);
    }

    @Override
    public void rejectRequest(AdminRequest request) {
        adminRequests.removeIf(r -> r.getId().equals(request.getId()));
    }

    @Override
    public Loan borrowBook(User user, Book book) {
        book.setStatus(Book.Status.BORROWED);
        Loan loan = new Loan(UUID.randomUUID().toString(), book, user, "2주 후 반납");
        loans.add(loan);
        reservations.removeIf(reservation -> reservation.getBook().getId().equals(book.getId()));
        return loan;
    }

    @Override
    public Reservation reserveBook(User user, Book book) {
        book.setStatus(Book.Status.RESERVED);
        Reservation reservation = new Reservation(UUID.randomUUID().toString(), book, user, "오늘 요청");
        reservations.add(reservation);
        adminRequests.add(new AdminRequest(UUID.randomUUID().toString(), AdminRequest.Type.RESERVATION_APPROVAL, user, book));
        return reservation;
    }

    @Override
    public void returnBook(User user, Book book) {
        book.setStatus(Book.Status.AVAILABLE);
        loans.removeIf(loan -> loan.getBook().getId().equals(book.getId()) && loan.getUser().getId().equals(user.getId()));
        adminRequests.add(new AdminRequest(UUID.randomUUID().toString(), AdminRequest.Type.RETURN_APPROVAL, user, book));
    }

    @Override
    public void cancelReservation(User user, Reservation reservation) {
        reservations.removeIf(r -> r.getId().equals(reservation.getId()));
        if (reservation.getBook().getStatus() == Book.Status.RESERVED) {
            reservation.getBook().setStatus(Book.Status.AVAILABLE);
        }
    }
}
