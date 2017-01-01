package com.example.jon.eventmeets.model;

/**
 * Created by Jon on 12/16/2016.
 */

public class BaseUser {
    public String username;
    public String firstName;
    public String lastName;

    public BaseUser(){}

    public BaseUser(String username, String firstName, String lastName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
