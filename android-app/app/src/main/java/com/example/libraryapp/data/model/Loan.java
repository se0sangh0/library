package com.example.libraryapp.data.model;

import java.io.Serializable;
public class Loan implements Serializable {
    private final String id;
    private final Book book;
    private final User user;
    private final String dueDate;

    public Loan(String id, Book book, User user, String dueDate) {
        this.id = id;
        this.book = book;
        this.user = user;
        this.dueDate = dueDate;
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

    public String getDueDate() {
        return dueDate;
    }
}
