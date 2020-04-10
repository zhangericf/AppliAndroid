package com.example.restaurantadvisor.restaurant;

public class Restaurant {

    protected String name, description, localization, phone_number, website, hours;
    protected float grade;
    protected int id;

    public Restaurant(int id, String name, String description, float grade, String localization, String phone_number, String website, String hours) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.grade = grade;
        this.localization = localization;
        this.phone_number = phone_number;
        this.website = website;
        this.hours = hours;
    }

    public Restaurant(){}

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }
}
