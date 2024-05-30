package com.example.projet_mobile;

import java.util.ArrayList;

public class User {
    private String lastname;
    private String firstname;
    private String email;
    private int phone;
    private String password;

    private static ArrayList<User> listUser;

    public User(String lastname, String firstname, String email, int phone, String password) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.phone = phone;
        this.password = password;
        listUser.add(this);
    }

    public User(String lastname, String firstname, String email, String password) {
        this(lastname, firstname, email, -1, password);
    }


}
