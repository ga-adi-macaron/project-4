package com.korbkenny.peoplesplaylist.objects;

import android.net.Uri;

/**
 * Created by KorbBookProReturns on 12/15/16.
 */

public class Song {
    private String mTitle, mSongId, mUserId, mPlaylistId, mUserImage;
    private Uri mStreamUrl;

    public Song() {
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
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

    public String getPlaylistId() {
        return mPlaylistId;
    }

    public void setPlaylistId(String playlistId) {
        mPlaylistId = playlistId;
    }

    public String getUserImage() {
        return mUserImage;
    }

    public void setUserImage(String userImage) {
        mUserImage = userImage;
    }
}
