package com.korbkenny.peoplesplaylist.objects;

import android.net.Uri;

/**
 * Created by KorbBookProReturns on 12/15/16.
 */

public class User {
    private String mUserName, mId, mUserImage;

    public User() {
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUserImage() {
        return mUserImage;
    }

    public void setUserImage(String userImage) {
        mUserImage = userImage;
    }
}
