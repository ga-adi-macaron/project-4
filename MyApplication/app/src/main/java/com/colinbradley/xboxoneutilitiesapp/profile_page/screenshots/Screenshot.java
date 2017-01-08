package com.colinbradley.xboxoneutilitiesapp.profile_page.screenshots;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by colinbradley on 12/19/16.
 */

public class Screenshot implements Parcelable{
    String title;
    String descripton;
    String imgURL;
    String game;

    public Screenshot(String title, String descripton, String imgURL, String game) {
        this.title = title;
        this.descripton = descripton;
        this.imgURL = imgURL;
        this.game = game;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescripton() {
        return descripton;
    }

    public void setDescripton(String descripton) {
        this.descripton = descripton;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

//-----------------------------
    //Parcelable Methods
//-----------------------------
    private Screenshot(Parcel in) {
        title = in.readString();
        descripton = in.readString();
        imgURL = in.readString();
        game = in.readString();
    }

    public static final Creator<Screenshot> CREATOR = new Creator<Screenshot>() {
        @Override
        public Screenshot createFromParcel(Parcel in) {
            return new Screenshot(in);
        }

        @Override
        public Screenshot[] newArray(int size) {
            return new Screenshot[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(descripton);
        parcel.writeString(imgURL);
        parcel.writeString(game);

    }
}
