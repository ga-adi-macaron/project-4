package com.example.jon.eventmeets.Model.GamingEventModels;

import com.example.jon.eventmeets.Model.EventParent;

/**
 * Created by Jon on 12/16/2016.
 */

public abstract class GamingEventParent implements EventParent {
    abstract String getTitle();
    abstract String getCategory();
    abstract boolean needsNearby();
    abstract boolean canBeNearby();

    public GamingEventParent(String categoryType) {

    }
    public GamingEventParent(){}
}
