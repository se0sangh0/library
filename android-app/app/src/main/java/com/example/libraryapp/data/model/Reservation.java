package com.example.libraryapp.data.model;

import java.io.Serializable;
public class Reservation implements Serializable {
    private final String id;
    private final Book book;
    private final User user;
    private final String requestDate;

    public Reservation(String id, Book book, User user, String requestDate) {
        this.id = id;
        this.book = book;
        this.user = user;
        this.requestDate = requestDate;
    }

    public String getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public User getUser() {
        return user;
    }

    public String getRequestDate() {
        return requestDate;
    }
}
