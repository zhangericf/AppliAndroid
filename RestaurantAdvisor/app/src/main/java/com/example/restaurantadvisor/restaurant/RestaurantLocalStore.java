package com.example.restaurantadvisor.restaurant;

import android.content.Context;
import android.content.SharedPreferences;

public class RestaurantLocalStore {

    private static final String SP_NAME = "restaurantDetails";
    private SharedPreferences restaurantLocalDatabase;

    public RestaurantLocalStore(Context context) {
        restaurantLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeRestaurantData(Restaurant restaurant) {
        SharedPreferences.Editor spEditor = restaurantLocalDatabase.edit();
        spEditor.putInt("id", restaurant.id);
        spEditor.putString("name", restaurant.name);
        spEditor.putString("hours", restaurant.hours);
        spEditor.putString("description", restaurant.description);
        spEditor.putString("localization", restaurant.localization);
        spEditor.putString("phone_number", restaurant.phone_number);
        spEditor.putString("website", restaurant.website);
        spEditor.putFloat("grade", restaurant.grade);
        spEditor.apply();
    }

    public Restaurant getRestaurant() {
        int id = restaurantLocalDatabase.getInt("id", 0);
        String hours = restaurantLocalDatabase.getString("hours", "");
        String name = restaurantLocalDatabase.getString("name", "");
        String description = restaurantLocalDatabase.getString("description", "");
        float grade = restaurantLocalDatabase.getFloat("grade", -1);
        String phone_number = restaurantLocalDatabase.getString("phone_number", "");
        String localization = restaurantLocalDatabase.getString("localization", "");
        String website = restaurantLocalDatabase.getString("website", "");

        return new Restaurant(id, name, description, grade, localization, phone_number, website, hours);
    }

    public void clearRestaurantData() {
        SharedPreferences.Editor spEditor = restaurantLocalDatabase.edit();
        spEditor.clear();
        spEditor.apply();
    }
}
