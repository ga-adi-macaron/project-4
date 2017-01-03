package com.colinbradley.xboxoneutilitiesapp.store_page.deals_with_gold;

/**
 * Created by colinbradley on 1/3/17.
 */

public class DealWithGold {
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
}
