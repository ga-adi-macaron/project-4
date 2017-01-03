package com.example.jon.eventmeets.model;

/**
 * Created by Jon on 1/3/2017.
 */

public class AvailablePlayer {
    private String mUserKey;
    private String mFirstName;
    private String mPhotoURL;

    public AvailablePlayer(String userKey, String firstName, String photoURL) {
        mUserKey = userKey;
        mFirstName = firstName;
        mPhotoURL = photoURL;
    }

    public String getUserKey() {
        return mUserKey;
    }

    public void setUserKey(String userKey) {
        mUserKey = userKey;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getPhotoURL() {
        return mPhotoURL;
    }

    public void setPhotoURL(String photoURL) {
        mPhotoURL = photoURL;
    }
}
