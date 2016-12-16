package com.example.jon.eventmeets.Model.TheaterEventModels;

import com.example.jon.eventmeets.Model.EventParent;

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
