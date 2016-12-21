package com.example.jon.eventmeets.model.game_models;

/**
 * Created by Jon on 12/16/2016.
 */

public class VideoGamingEvent extends GamingEventParent {
    private String mTitle;
    private String mCategory;
    private String mSubCategory;
    private boolean mNeedsNearby;
    private boolean mCanBeNearby;
    private String mLocation;
    private String mTime;

    /**
     * Event constructor with only mandatory parameters
     * @param title The unique name of the event
     * @param category The type of event as a string, see Category for more details
     * @param needsNearby If the event requires participants to be local (i.e. hosted in-person)
     */
    public VideoGamingEvent(String title, String category, boolean needsNearby) {
        mTitle = title;
        mCategory = category;
        mNeedsNearby = needsNearby;
        mTime = "TBD";
        mSubCategory = "none";
        //Checking for nearby selection
        if(mNeedsNearby) {
            mLocation = "show error";
        }
    }

    /**
     * The full constructor for an event with all fields filled out.
     * @param title
     * @param category
     * @param subCategory
     * @param needsNearby
     * @param canBeNearby
     * @param location
     * @param time
     */
    public VideoGamingEvent(String title, String category, String subCategory, boolean needsNearby, boolean canBeNearby,
                            String location, String time) {
        mTitle = title;
        mCategory = category;
        mSubCategory = subCategory;
        mNeedsNearby = needsNearby;
        mCanBeNearby = canBeNearby;
        mLocation = location;
        mTime = time;
    }

    /**
     * Constructor called if the event needs nearby users
     * @param title
     * @param category
     * @param needsNearby
     * @param location
     */
    public VideoGamingEvent(String title, String category, boolean needsNearby, String location) {
        mTitle = title;
        mCategory = category;
        mNeedsNearby = needsNearby;
        mLocation = location;
        mSubCategory = "none";
    }

    private void nearbyLocationSelected() {
        if(mNeedsNearby) {
            mLocation = null; //Location becomes required
        }
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
    public boolean needsNearby() {
        return mNeedsNearby;
    }

    @Override
    public boolean canBeNearby() {
        return mCanBeNearby;
    }

    public String getSubCategory() {
        return mSubCategory;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getTime() {
        return mTime;
    }

    public void setSubCategory(String category) {
        mSubCategory = category;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public void setCanbeNearby(boolean canBeNearby) {
        mCanBeNearby = canBeNearby;
    }

    @Override
    public String getChildren() {
        return null;
    }
}
