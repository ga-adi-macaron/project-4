package com.example.jon.eventmeets.Model;

import java.util.ArrayList;

/**
 * Created by Jon on 12/16/2016.
 */

public class BaseUser {
    private String mFirstName;
    private String mLastName;
    private ArrayList<EventParent> mSavedEvents;
    private ArrayList<EventParent> mScheduledEvents;
    private String mUsername;
    private String mCurrentLocation;

    // TODO: Convert to a singleton which stores user data & instantiate with data during login
    public BaseUser(String firstName, String lastName, String username) {
        mFirstName = firstName;
        mLastName = lastName;
        mUsername = username;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public ArrayList<EventParent> getSavedEvents() {
        return mSavedEvents;
    }

    public void saveNewEvent(EventParent savedEvent) {
        mSavedEvents.add(savedEvent);
    }

    public void saveNewEvent(int index, EventParent savedEvent) {
        mSavedEvents.add(index, savedEvent);
    }

    public ArrayList<EventParent> getScheduledEvents() {
        return mScheduledEvents;
    }

    public void scheduleNewEvent(EventParent scheduledEvent) {
        mScheduledEvents.add(scheduledEvent);
    }

    public String getUsername() {
        return mUsername;
    }

    public String getCurrentLocation() {
        return mCurrentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        mCurrentLocation = currentLocation;
    }
}
