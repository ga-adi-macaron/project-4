package com.example.jon.eventmeets.model.game_models;

import com.example.jon.eventmeets.model.EventParent;

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
