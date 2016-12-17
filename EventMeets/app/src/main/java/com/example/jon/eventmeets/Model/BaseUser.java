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
//    public BaseUser(String firstName, String lastName, String username) {
//        mFirstName = firstName;
//        mLastName = lastName;
//        mUsername = username;
//    }

    private static BaseUser sInstance = null;

    public static BaseUser getInstance() {
        if(sInstance == null) {
            sInstance = new BaseUser();
        }
        return sInstance;
    }

    private BaseUser() {
    }

    private String getFirstName() {
        return mFirstName;
    }

    private String getLastName() {
        return mLastName;
    }

    private ArrayList<EventParent> getSavedEvents() {
        return mSavedEvents;
    }

    private void saveNewEvent(EventParent savedEvent) {
        mSavedEvents.add(savedEvent);
    }

    private void saveNewEvent(int index, EventParent savedEvent) {
        mSavedEvents.add(index, savedEvent);
    }

    private ArrayList<EventParent> getScheduledEvents() {
        return mScheduledEvents;
    }

    private void scheduleNewEvent(EventParent scheduledEvent) {
        mScheduledEvents.add(scheduledEvent);
    }

    private String getUsername() {
        return mUsername;
    }

    private String getCurrentLocation() {
        return mCurrentLocation;
    }

    private void setCurrentLocation(String currentLocation) {
        mCurrentLocation = currentLocation;
    }
}
