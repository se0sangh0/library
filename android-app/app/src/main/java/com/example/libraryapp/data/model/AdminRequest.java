package com.example.libraryapp.data.model;

import java.io.Serializable;

public class AdminRequest implements Serializable {
    public enum Type {
        RESERVATION_APPROVAL,
        RETURN_APPROVAL
    }

    private final String id;
    private final Type type;
    private final User requester;
    private final Book book;

    public AdminRequest(String id, Type type, User requester, Book book) {
        this.id = id;
        this.type = type;
        this.requester = requester;
        this.book = book;
    }

    public String getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public User getRequester() {
        return requester;
    }

    public Book getBook() {
        return book;
    }
}
