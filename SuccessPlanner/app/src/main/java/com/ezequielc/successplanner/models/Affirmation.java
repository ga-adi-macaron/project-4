package com.ezequielc.successplanner.models;

/**
 * Created by student on 12/20/16.
 */

public class Affirmation {
    private long mID;
    private String mDate;
    private String mAffirmation;

    public Affirmation(long mID, String mDate, String mAffirmation) {
        this.mID = mID;
        this.mDate = mDate;
        this.mAffirmation = mAffirmation;
    }

    public long getID() {
        return mID;
    }

    public void setID(long mID) {
        this.mID = mID;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getAffirmation() {
        return mAffirmation;
    }

    public void setAffirmation(String mAffirmation) {
        this.mAffirmation = mAffirmation;
    }
}
