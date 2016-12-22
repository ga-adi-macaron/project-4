package com.ezequielc.successplanner.models;

/**
 * Created by student on 12/20/16.
 */

public class Goal {
    private long mID;
    private String mDate;
    private String mGoal;

    public Goal(long mID, String mDate, String mGoal) {
        this.mID = mID;
        this.mDate = mDate;
        this.mGoal = mGoal;
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

    public String getGoal() {
        return mGoal;
    }

    public void setGoal(String mGoal) {
        this.mGoal = mGoal;
    }
}
