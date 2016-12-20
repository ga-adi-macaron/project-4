package com.example.jon.eventmeets.model.drink_models;

import com.example.jon.eventmeets.model.EventParent;

/**
 * Created by Jon on 12/16/2016.
 */

public class DrinkEventParent implements EventParent {
    @Override
    public String getParent() {
        return "Drinking";
    }
}
