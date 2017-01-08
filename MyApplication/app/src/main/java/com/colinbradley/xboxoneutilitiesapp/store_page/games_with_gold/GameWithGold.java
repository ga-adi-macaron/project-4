package com.colinbradley.xboxoneutilitiesapp.store_page.games_with_gold;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by colinbradley on 12/29/16.
 */

public class GameWithGold implements Parcelable{

    private String mTitle;
    private String mOriginalPrice;
    private String mURLforBoxArt;
    private String mGameID;

    public GameWithGold(String mTitle, String mOriginalPrice, String mURLforBoxArt, String mGameID) {
        this.mTitle = mTitle;
        this.mOriginalPrice = mOriginalPrice;
        this.mURLforBoxArt = mURLforBoxArt;
        this.mGameID = mGameID;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmOriginalPrice() {
        return mOriginalPrice;
    }

    public void setmOriginalPrice(String mOriginalPrice) {
        this.mOriginalPrice = mOriginalPrice;
    }

    public String getmURLforBoxArt() {
        return mURLforBoxArt;
    }

    public void setmURLforBoxArt(String mURLforBoxArt) {
        this.mURLforBoxArt = mURLforBoxArt;
    }

    public String getmGameID() {
        return mGameID;
    }

    public void setmGameID(String mGameID) {
        this.mGameID = mGameID;
    }

//-----------------------------
    //Parcelable Methods
//-----------------------------

    private GameWithGold(Parcel in) {
        mTitle = in.readString();
        mOriginalPrice = in.readString();
        mURLforBoxArt = in.readString();
        mGameID = in.readString();
    }

    public static final Creator<GameWithGold> CREATOR = new Creator<GameWithGold>() {
        @Override
        public GameWithGold createFromParcel(Parcel in) {
            return new GameWithGold(in);
        }

        @Override
        public GameWithGold[] newArray(int size) {
            return new GameWithGold[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mOriginalPrice);
        parcel.writeString(mURLforBoxArt);
        parcel.writeString(mGameID);
    }
}
