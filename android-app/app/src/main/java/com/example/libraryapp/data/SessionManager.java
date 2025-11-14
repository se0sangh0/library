package com.example.libraryapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.example.libraryapp.data.model.User;
import com.google.gson.Gson;

public class SessionManager {
    private static final String PREFS_NAME = "library_app_prefs";
    private static final String KEY_USER = "current_user";

    private final SharedPreferences preferences;
    private final Gson gson = new Gson();

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setCurrentUser(@Nullable User user) {
        if (user == null) {
            preferences.edit().remove(KEY_USER).apply();
        } else {
            preferences.edit().putString(KEY_USER, gson.toJson(user)).apply();
        }
    }

    @Nullable
    public User getCurrentUser() {
        String json = preferences.getString(KEY_USER, null);
        if (json == null) {
            return null;
        }
        return gson.fromJson(json, User.class);
    }

    public void clear() {
        preferences.edit().clear().apply();
    }
}
