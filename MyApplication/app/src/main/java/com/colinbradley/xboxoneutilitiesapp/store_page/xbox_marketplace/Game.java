package com.colinbradley.xboxoneutilitiesapp.store_page.xbox_marketplace;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by colinbradley on 12/29/16.
 */

public class Game implements Parcelable{

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


//-----------------------------
    //Parcelable Methods
//-----------------------------

    private Game(Parcel in) {
        mTitle = in.readString();
        mGameID = in.readString();
        mMainImgURL = in.readString();
        mDevName = in.readString();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mGameID);
        parcel.writeString(mMainImgURL);
        parcel.writeString(mDevName);
    }
}
