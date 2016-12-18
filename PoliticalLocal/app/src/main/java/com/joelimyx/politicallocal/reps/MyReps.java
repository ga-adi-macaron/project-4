package com.joelimyx.politicallocal.reps;

/**
 * Created by Joe on 12/17/16.
 */

public class MyReps {
    private String mBioId, mCId, mName, mParty, mPhone, mChamber;
    private int mDistrictClass;

    public MyReps(String bioId, String CId, String name, String party, String phone, int districtClass, String chamber) {
        mBioId = bioId;
        mCId = CId;
        mName = name;
        mParty = party;
        mPhone = phone;
        mDistrictClass = districtClass;
        mChamber = chamber;
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

    public int getDistrictClass() {
        return mDistrictClass;
    }

    public String getChamber() {
        return mChamber;
    }
}
