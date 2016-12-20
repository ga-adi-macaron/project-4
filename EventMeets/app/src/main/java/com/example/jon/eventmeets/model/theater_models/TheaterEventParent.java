package com.example.jon.eventmeets.model.theater_models;

import com.example.jon.eventmeets.model.EventParent;

/**
 * Created by Jon on 12/16/2016.
 */

public abstract class TheaterEventParent implements EventParent {
    abstract String getGenre();
    abstract String getLocation();
    abstract boolean hasSpecificChoice();
    abstract String getMaxDuration();
    abstract String getEstimatedDuration();
}
