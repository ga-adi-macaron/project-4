package com.example.jon.eventmeets.model;

/**
 * Created by Jon on 1/3/2017.
 */

public class AvailablePlayer {
    private String user;
    private String firstName;

    public AvailablePlayer(String user, String firstName) {
        this.user = user;
        this.firstName = firstName;
    }

    public AvailablePlayer() {}

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
