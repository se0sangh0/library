package com.example.libraryapp;

import android.app.Application;

import com.example.libraryapp.data.InMemoryLibraryRepository;
import com.example.libraryapp.data.LibraryRepository;
import com.example.libraryapp.data.SessionManager;

public class LibraryApplication extends Application {
    private LibraryRepository repository;
    private SessionManager sessionManager;

    @Override
    public void onCreate() {
        super.onCreate();
        repository = new InMemoryLibraryRepository();
        sessionManager = new SessionManager(this);
    }

    public LibraryRepository getRepository() {
        return repository;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
