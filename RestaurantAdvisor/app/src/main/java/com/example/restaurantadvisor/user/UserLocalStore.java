package com.example.restaurantadvisor.user;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLocalStore {

    private static final String SP_NAME = "userDetails";
    private SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putInt("id", user.id);
        spEditor.putString("name", user.name);
        spEditor.putString("email", user.email);
        spEditor.putString("firstname", user.firstname);
        spEditor.putString("login", user.login);
        spEditor.putString("password", user.password);
        spEditor.putInt("age", user.age);
        spEditor.apply();
    }

    public User getLoggedInUser() {
        int id = userLocalDatabase.getInt("id", 0);
        String name = userLocalDatabase.getString("name", "");
        String email = userLocalDatabase.getString("email", "");
        int age = userLocalDatabase.getInt("age", -1);
        String login = userLocalDatabase.getString("login", "");
        String firstname = userLocalDatabase.getString("firstname", "");
        String password = userLocalDatabase.getString("password", "");

        return new User(id, login, email, name, firstname, password, age);
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.apply();
    }

    public boolean getUserLoggedIn() {
        if (userLocalDatabase.getBoolean("loggedIn", false))
            return true;
        return false;
    }

    public void clearUserData() {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.apply();
    }
}
