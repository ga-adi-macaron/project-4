package com.example.jon.eventmeets.Model;

import java.util.List;

/**
 * Created by Jon on 12/16/2016.
 */

public class BaseUser {
    public String username;
    public String firstName;

    public BaseUser(){}

    public BaseUser(String username, String firstName) {
        this.username = username;
        this.firstName = firstName;
    }
}
