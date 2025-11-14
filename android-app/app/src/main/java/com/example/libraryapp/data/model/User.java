package com.example.libraryapp.data.model;

import java.io.Serializable;

public class User implements Serializable {
    public enum Role {
        MEMBER,
        ADMIN
    }

    private final String id;
    private final String name;
    private final String email;
    private final Role role;

    public User(String id, String name, String email, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}
