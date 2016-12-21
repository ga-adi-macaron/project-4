package com.colinbradley.xboxoneutilitiesapp.profile_page.gameclips;

/**
 * Created by colinbradley on 12/19/16.
 */

public class GameClip {
    String clipName;
    String gameName;
    String clipDescription;
    String clipURL;
    String imgURL;

    public GameClip(String clipName, String gameName, String clipDescription, String clipURL, String imgURL) {
        this.clipName = clipName;
        this.gameName = gameName;
        this.clipDescription = clipDescription;
        this.clipURL = clipURL;
        this.imgURL = imgURL;
    }

    public String getClipName() {
        return clipName;
    }

    public void setClipName(String clipName) {
        this.clipName = clipName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getClipDescription() {
        return clipDescription;
    }

    public void setClipDescription(String clipDescription) {
        this.clipDescription = clipDescription;
    }

    public String getClipURL() {
        return clipURL;
    }

    public void setClipURL(String clipURL) {
        this.clipURL = clipURL;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
