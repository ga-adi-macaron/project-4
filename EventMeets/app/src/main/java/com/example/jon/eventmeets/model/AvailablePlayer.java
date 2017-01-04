package com.example.jon.eventmeets.model;

/**
 * Created by Jon on 1/3/2017.
 */

public class AvailablePlayer {
    private String user;
    private String display;

    public AvailablePlayer(String user, String display) {
        this.user = user;
        this.display = display;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
