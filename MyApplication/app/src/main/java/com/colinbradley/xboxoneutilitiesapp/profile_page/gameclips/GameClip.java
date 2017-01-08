package com.colinbradley.xboxoneutilitiesapp.profile_page.gameclips;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by colinbradley on 12/19/16.
 */

public class GameClip implements Parcelable{
    String mClipName;
    String mGameName;
    String mClipDescription;
    String mClipURL;
    String mImgURL;

    public GameClip(String clipName, String gameName, String clipDescription, String clipURL, String imgURL) {
        this.mClipName = clipName;
        this.mGameName = gameName;
        this.mClipDescription = clipDescription;
        this.mClipURL = clipURL;
        this.mImgURL = imgURL;
    }

    public String getClipName() {
        return mClipName;
    }

    public void setClipName(String clipName) {
        this.mClipName = clipName;
    }

    public String getmGameName() {
        return mGameName;
    }

    public void setmGameName(String mGameName) {
        this.mGameName = mGameName;
    }

    public String getClipDescription() {
        return mClipDescription;
    }

    public void setClipDescription(String clipDescription) {
        this.mClipDescription = clipDescription;
    }

    public String getmClipURL() {
        return mClipURL;
    }

    public void setmClipURL(String mClipURL) {
        this.mClipURL = mClipURL;
    }

    public String getmImgURL() {
        return mImgURL;
    }

    public void setmImgURL(String mImgURL) {
        this.mImgURL = mImgURL;
    }

//-----------------------------
    //Parcelable Methods
//-----------------------------
    private GameClip(Parcel in) {
        mClipName = in.readString();
        mGameName = in.readString();
        mClipDescription = in.readString();
        mClipURL = in.readString();
        mImgURL = in.readString();
    }

    public static final Creator<GameClip> CREATOR = new Creator<GameClip>() {
        @Override
        public GameClip createFromParcel(Parcel in) {
            return new GameClip(in);
        }

        @Override
        public GameClip[] newArray(int size) {
            return new GameClip[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mClipDescription);
        parcel.writeString(mClipName);
        parcel.writeString(mClipURL);
        parcel.writeString(mGameName);
        parcel.writeString(mImgURL);
    }
}
