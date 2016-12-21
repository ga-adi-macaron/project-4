package com.colinbradley.xboxoneutilitiesapp.profile_page.friends_list;

import java.util.Comparator;

/**
 * Created by colinbradley on 12/19/16.
 */

public class Friend implements Comparable<Friend>{
    private String gamertag;
    private String picURL;
    private int xuid;

    public static final Comparator<Friend> gamertagComparator = new Comparator<Friend>() {
        @Override
        public int compare(Friend friend, Friend t1) {
            return friend.getGamertag().compareToIgnoreCase(t1.getGamertag());
        }
    };

    public Friend(String gamertag, String picURL, int xuid) {
        this.gamertag = gamertag;
        this.picURL = picURL;
        this.xuid = xuid;
    }

    public String getGamertag() {
        return gamertag;
    }

    public void setGamertag(String gamertag) {
        this.gamertag = gamertag;
    }

    public String getPicURL() {
        return picURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }

    public int getXuid() {
        return xuid;
    }

    public void setXuid(int xuid) {
        this.xuid = xuid;
    }


    @Override
    public int compareTo(Friend friend) {
        return this.xuid - friend.xuid;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (getClass() != obj.getClass()){
            return false;
        }
        final Friend other = (Friend) obj;
        if (this.xuid != other.xuid){
            return false;
        }
        if ((this.gamertag == null) ? (other.gamertag != null) : !this.gamertag.equals(other.gamertag)){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + this.xuid;
        hash = 47 * hash + (this.gamertag != null ? this.gamertag.hashCode() : 0);
        return hash;
    }
}
