package com.ezequielc.successplanner.models;

import java.util.ArrayList;

/**
 * Created by student on 12/20/16.
 */

public class DailyData {
    private long mID;
    private String mDate;
    private ArrayList<Goal> mGoals;
    private ArrayList<Affirmation> mAffirmations;
    private ArrayList<Schedule> mSchedule;

    public DailyData(String mDate, ArrayList<String> mGoals,
                     ArrayList<String> mAffirmations, ArrayList<String> mSchedule) {
        this.mDate = mDate;
        this.mGoals = new ArrayList<>();
        this.mAffirmations = new ArrayList<>();
        this.mSchedule = new ArrayList<>();
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

    public ArrayList<Goal> getGoals() {
        return mGoals;
    }

    public void setGoals(ArrayList<Goal> mGoals) {
        this.mGoals = mGoals;
    }

    public ArrayList<Affirmation> getAffirmations() {
        return mAffirmations;
    }

    public void setAffirmations(ArrayList<Affirmation> mAffirmations) {
        this.mAffirmations = mAffirmations;
    }

    public ArrayList<Schedule> getSchedule() {
        return mSchedule;
    }

    public void setSchedule(ArrayList<Schedule> mSchedule) {
        this.mSchedule = mSchedule;
    }
}
