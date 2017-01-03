package com.example.jon.eventmeets.model;

/**
 * Created by Jon on 12/16/2016.
 */

public class BaseUser {
    private String username;
    private String firstName;
    private String lastName;

    private BaseUser(){}

    private static BaseUser sInstance = null;

    public static BaseUser getInstance() {
        if(sInstance == null) {
            sInstance = new BaseUser();
        }
        return sInstance;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
