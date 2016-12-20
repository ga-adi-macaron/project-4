package com.example.jon.eventmeets.model.nature_models;

import com.example.jon.eventmeets.model.EventParent;

/**
 * Created by Jon on 12/16/2016.
 */

public abstract class NatureEventParent implements EventParent {
    abstract String getTitle();
    abstract String getCategory();
    abstract String getSubCategory();
    abstract String getLocation();
}
