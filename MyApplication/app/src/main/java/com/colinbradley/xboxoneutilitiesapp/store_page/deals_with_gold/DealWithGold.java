package com.colinbradley.xboxoneutilitiesapp.store_page.deals_with_gold;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by colinbradley on 1/3/17.
 */

public class DealWithGold implements Parcelable{
    private String mTitle;
    private String mOriginalPrice;
    private String mNewPrice;
    private String mURLforBoxArt;
    private String mGameID;

    public DealWithGold(String Title, String OriginalPrice, String URLforBoxArt, String GameID, String NewPrice) {
        this.mTitle = Title;
        this.mOriginalPrice = OriginalPrice;
        this.mURLforBoxArt = URLforBoxArt;
        this.mGameID = GameID;
        this.mNewPrice = NewPrice;
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

    public String getmNewPrice() {
        return mNewPrice;
    }

    public void setmNewPrice(String mNewPrice) {
        this.mNewPrice = mNewPrice;
    }

//-----------------------------
    //Parcelable Methods
//-----------------------------

    private DealWithGold(Parcel in) {
        mTitle = in.readString();
        mOriginalPrice = in.readString();
        mNewPrice = in.readString();
        mURLforBoxArt = in.readString();
        mGameID = in.readString();
    }

    public static final Creator<DealWithGold> CREATOR = new Creator<DealWithGold>() {
        @Override
        public DealWithGold createFromParcel(Parcel in) {
            return new DealWithGold(in);
        }

        @Override
        public DealWithGold[] newArray(int size) {
            return new DealWithGold[size];
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
        parcel.writeString(mNewPrice);
    }
}
