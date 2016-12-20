package com.example.jon.eventmeets.Model.DrinkEventModels;

import com.example.jon.eventmeets.Model.EventParent;

/**
 * Created by Jon on 12/16/2016.
 */

public class DrinkEventParent implements EventParent {
    @Override
    public String getParent() {
        return "Drinking";
    }
}
