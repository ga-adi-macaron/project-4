package com.korbkenny.peoplesplaylist.objects;

import java.net.URL;
import java.util.List;

/**
 * Created by KorbBookProReturns on 12/15/16.
 */
public class Playlist {

    private double mLatitude, mLongitude;
    private String mTitle, mDescription, mId, mUserId, mCover, mIcon;

    //  If you use an empty constructor and push a playlist to the database,
    //  you can get that entire playlist back with a single query.
    public Playlist(){}

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public double getLat() {
        return mLatitude;
    }

    public void setLat(double latitude) {
        mLatitude = latitude;
    }

    public double getLon() {
        return mLongitude;
    }

    public void setLon(double longitude) {
        mLongitude = longitude;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getCover() {
        return mCover;
    }

    public void setCover(String cover) {
        mCover = cover;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }
}
