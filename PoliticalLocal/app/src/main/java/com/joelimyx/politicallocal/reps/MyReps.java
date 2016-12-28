package com.joelimyx.politicallocal.reps;

import android.support.annotation.Nullable;

/**
 * Created by Joe on 12/17/16.
 */

public class MyReps {
    private String mBioId, mCId, mName, mParty, mPhone,mEmail,mWebsite, mTwitter, mChamber, mFileName;
    private int mDistrictClass;

    public MyReps(String bioId, String CId, String name, String party, String chamber, int districtClass, @Nullable String phone, @Nullable String email, @Nullable String website, @Nullable String twitter, @Nullable String fileName) {
        mBioId = bioId;
        mCId = CId;
        mName = name;
        mParty = party;
        mPhone = phone;
        mEmail = email;
        mWebsite = website;
        mTwitter = twitter;
        mChamber = chamber;
        mDistrictClass = districtClass;
        mFileName = fileName;
    }

    public String getBioId() {
        return mBioId;
    }

    public String getCId() {
        return mCId;
    }

    public String getName() {
        if (mChamber.equalsIgnoreCase("house")) {
            return "Rep. "+mName;
        }
        return "Sen. "+mName;
    }

    public String getParty() {
        return mParty;
    }

    public String getPhone() {
        return mPhone;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public String getTwitter() {
        return mTwitter;
    }

    public int getDistrictClass() {
        return mDistrictClass;
    }

    public String getChamber() {
        return mChamber;
    }

    public String getFileName() {
        return mFileName;
    }
}
