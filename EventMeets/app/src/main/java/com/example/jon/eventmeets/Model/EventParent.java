package com.example.jon.eventmeets.model;

/**
 * Created by Jon on 12/16/2016.
 */

public interface EventParent {
    /**
     * Returns an array of Strings for the category, or null if no child categories exist
     */
    String[] getChildren();
//    String getLocation();
//    void setIcon(View view);
}
