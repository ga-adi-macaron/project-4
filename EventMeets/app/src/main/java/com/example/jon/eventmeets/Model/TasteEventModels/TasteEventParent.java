package com.example.jon.eventmeets.Model.TasteEventModels;

import com.example.jon.eventmeets.Model.EventParent;

/**
 * Created by Jon on 12/16/2016.
 */

public abstract class TasteEventParent implements EventParent {
    abstract int getPriceRating();
    abstract String getCuisine();
    abstract boolean isHomemade();
    abstract String getLocation();
}
