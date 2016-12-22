package com.example.jon.eventmeets.model.taste_models;

import com.example.jon.eventmeets.model.EventParent;

/**
 * Created by Jon on 12/16/2016.
 */

public class TasteEventParent implements EventParent {

    public TasteEventParent(){}

    @Override
    public String[] getChildren() {
        return new String[]{"Let's cook!", "Fine Dining", "Hidden Gems < $10", "Unique Tastes"};
    }
}
