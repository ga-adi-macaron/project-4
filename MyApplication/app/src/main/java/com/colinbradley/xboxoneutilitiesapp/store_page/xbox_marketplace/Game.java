package com.colinbradley.xboxoneutilitiesapp.store_page.xbox_marketplace;

/**
 * Created by colinbradley on 12/29/16.
 */

public class Game {

    String mTitle;
    String mGameID;
    String mMainImgURL;
    String mDevName;

    public Game(String mTitle, String mGameID, String mMainImgURL, String mDevName) {
        this.mTitle = mTitle;
        this.mGameID = mGameID;
        this.mMainImgURL = mMainImgURL;
        this.mDevName = mDevName;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmGameID() {
        return mGameID;
    }

    public void setmGameID(String mGameID) {
        this.mGameID = mGameID;
    }

    public String getmMainImgURL() {
        return mMainImgURL;
    }

    public void setmMainImgURL(String mMainImgURL) {
        this.mMainImgURL = mMainImgURL;
    }

    public String getmDevName() {
        return mDevName;
    }

    public void setmDevName(String mDevName) {
        this.mDevName = mDevName;
    }
}
