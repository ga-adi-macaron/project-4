package com.colinbradley.xboxoneutilitiesapp.profile_page.screenshots;

/**
 * Created by colinbradley on 12/19/16.
 */

public class Screenshot {
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
}
