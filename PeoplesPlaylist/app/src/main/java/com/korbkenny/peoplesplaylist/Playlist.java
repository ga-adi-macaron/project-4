package com.korbkenny.peoplesplaylist;

import android.graphics.Bitmap;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KorbBookProReturns on 12/15/16.
 */
public class Playlist {

    private double mLatitude, mLongitude;
    private String mTitle, mDescription;
    private URL mCover;
    private List<Song> mSongList;

    public Playlist(){}

//    public Playlist(double latitude, double longitude, String title, String description) {
//        mLatitude = latitude;
//        mLongitude = longitude;
//        mTitle = title;
//        mDescription = description;
//        mSongList = new ArrayList<>();
//    }

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

    public URL getCover() {
        return mCover;
    }

    public void setCover(URL cover) {
        mCover = cover;
    }

    public List<Song> getSongList() {
        return mSongList;
    }
}
