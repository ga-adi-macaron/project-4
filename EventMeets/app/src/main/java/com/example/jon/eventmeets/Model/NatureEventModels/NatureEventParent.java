package com.example.jon.eventmeets.Model.NatureEventModels;

import com.example.jon.eventmeets.Model.EventParent;

/**
 * Created by Jon on 12/16/2016.
 */

public abstract class NatureEventParent implements EventParent {
    abstract String getTitle();
    abstract String getCategory();
    abstract String getSubCategory();
    abstract String getLocation();
}
