package com.example.jon.eventmeets.model.game_models;

import com.example.jon.eventmeets.model.EventParent;

/**
 * Created by Jon on 12/16/2016.
 */

public class GamingEventParent implements EventParent {
    public GamingEventParent(){}

    @Override
    public String[] getChildren() {
        return new String[]{"Video Games, Board Games, Card Games"};
    }
}
