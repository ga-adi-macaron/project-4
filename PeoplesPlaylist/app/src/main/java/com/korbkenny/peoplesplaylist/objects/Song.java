package com.korbkenny.peoplesplaylist.objects;

import android.net.Uri;

/**
 * Created by KorbBookProReturns on 12/15/16.
 */

public class Song {
    private User mUser;
    private String mTitle, mSongId;
    private Uri mStreamUrl;

    public Song() {
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Uri getStreamUrl() {
        return mStreamUrl;
    }

    public void setStreamUrl(Uri streamUrl) {
        mStreamUrl = streamUrl;
    }

    public String getSongId() {
        return mSongId;
    }

    public void setSongId(String songId) {
        mSongId = songId;
    }
}
