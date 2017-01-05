package com.colinbradley.xboxoneutilitiesapp.store_page.games_with_gold;

/**
 * Created by colinbradley on 12/29/16.
 */

public class GameWithGold {

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
}
