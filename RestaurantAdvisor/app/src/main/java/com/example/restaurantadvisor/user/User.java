package com.example.restaurantadvisor.user;

public class User {

    protected String login, email, name, firstname, password;
    protected int age, id;

    public User(int id, String login, String email, String name, String firstname, String password, int age) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.name = name;
        this.firstname = firstname;
        this.password = password;
        this.age = age;
    }

    public User(String login, String password) {
        this.login = login;
        this.email = "";
        this.name = "";
        this.firstname = "";
        this.password = password;
        this.age = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
