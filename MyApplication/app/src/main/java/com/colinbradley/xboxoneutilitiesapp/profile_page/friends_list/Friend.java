package com.colinbradley.xboxoneutilitiesapp.profile_page.friends_list;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

/**
 * Created by colinbradley on 12/19/16.
 */

public class Friend implements Comparable<Friend>, Parcelable{
    private String mGamertag;
    private String mPicURL;
    private long mXuid;

    public static final Comparator<Friend> gamertagComparator = new Comparator<Friend>() {
        @Override
        public int compare(Friend friend, Friend t1) {
            return friend.getGamertag().compareToIgnoreCase(t1.getGamertag());
        }
    };

    public Friend(String gamertag, String picURL, long xuid) {
        this.mGamertag = gamertag;
        this.mPicURL = picURL;
        this.mXuid = xuid;
    }

    public String getGamertag() {
        return mGamertag;
    }

    public void setGamertag(String gamertag) {
        this.mGamertag = gamertag;
    }

    public String getPicURL() {
        return mPicURL;
    }

    public void setPicURL(String picURL) {
        this.mPicURL = picURL;
    }

    public long getXuid() {
        return mXuid;
    }

    public void setXuid(long xuid) {
        this.mXuid = xuid;
    }

//----------------------------
    //Comparator Methods
//----------------------------
    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (getClass() != obj.getClass()){
            return false;
        }
        final Friend other = (Friend) obj;
        if (this.mXuid != other.mXuid){
            return false;
        }
        if ((this.mGamertag == null) ? (other.mGamertag != null) : !this.mGamertag.equals(other.mGamertag)){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + (this.mGamertag != null ? this.mGamertag.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(Friend friend) {
        return 0;
    }

//-----------------------------
    //Parcelable Methods
//-----------------------------
    public static final Parcelable.Creator<Friend> CREATOR = new Creator<Friend>() {
        @Override
        public Friend createFromParcel(Parcel parcel) {
            return new Friend(parcel);
        }

        @Override
        public Friend[] newArray(int i) {
            return new Friend[i];
        }
    };

    private Friend(Parcel in){
        mGamertag = in.readString();
        mPicURL = in.readString();
        mXuid = in.readLong();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mGamertag);
        parcel.writeString(mPicURL);
        parcel.writeLong(mXuid);
    }
}
