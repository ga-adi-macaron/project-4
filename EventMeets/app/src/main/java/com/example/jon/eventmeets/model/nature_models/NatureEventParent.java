package com.example.jon.eventmeets.model.nature_models;

import com.example.jon.eventmeets.model.EventParent;

/**
 * Created by Jon on 12/16/2016.
 */

public class NatureEventParent implements EventParent {

    public NatureEventParent(){}

    @Override
    public String[] getChildren() {
        return new String[]{"Hiking", "Wheel to Trail", "Water-based", "Sightseeing", "Geocaching"};
    }
}
