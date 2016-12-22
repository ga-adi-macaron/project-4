package com.example.jon.eventmeets.model.theater_models;

import com.example.jon.eventmeets.model.EventParent;

/**
 * Created by Jon on 12/16/2016.
 */

public class TheaterEventParent implements EventParent {

    public TheaterEventParent(){}

    @Override
    public String[] getChildren() {
        return new String[]{"New Movies", "Private Viewings", "Musical Theater", };
    }
}
