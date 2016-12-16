package com.example.jon.eventmeets.Model.NatureEventModels;

import com.example.jon.eventmeets.Model.BaseUser;
import com.example.jon.eventmeets.Model.NatureEventModels.NatureEventParent;

import java.util.ArrayList;

/**
 * Created by Jon on 12/16/2016.
 */

public class HikingEvent extends NatureEventParent {
    private static final String SUB_CATEGORY = "hiking";

    private String mTitle;
    private String mCategory;
    private String mLocation;
    private String mTime;
    private ArrayList<BaseUser> mAttendees;

    public HikingEvent(String title, String category, String location) {
        mTitle = title;
        mCategory = category;
        mLocation = location;
    }

    public HikingEvent(String title, String category, String location, String time) {
        mTitle = title;
        mCategory = category;
        mLocation = location;
        mTime = time;
    }

    public void addMember(BaseUser user) {
        mAttendees.add(user);
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getCategory() {
        return mCategory;
    }

    @Override
    public String getSubCategory() {
        return SUB_CATEGORY;
    }

    @Override
    public String getLocation() {
        return mLocation;
    }
}
