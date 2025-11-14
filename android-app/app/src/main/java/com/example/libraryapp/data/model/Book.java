package com.example.libraryapp.data.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Book implements Serializable {
    public enum Status {
        AVAILABLE,
        RESERVED,
        BORROWED
    }

    private final String id;
    private final String title;
    private final String author;
    private final String description;
    private Status status;

    public Book(String id, String title, String author, String description, Status status) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @NonNull
    @Override
    public String toString() {
        return title + " - " + author;
    }
}
