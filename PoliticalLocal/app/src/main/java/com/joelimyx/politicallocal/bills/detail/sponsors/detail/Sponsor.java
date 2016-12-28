package com.joelimyx.politicallocal.bills.detail.sponsors.detail;

/**
 * Created by Joe on 12/27/16.
 */

public class Sponsor {
    private String mBioId, mName, mState, mParty, mChamber;
    private int mDistrictRank;
    private boolean mIsMajor;

    public Sponsor(String bioId, String name, String state, String party, String chamber, int districtRank, boolean isMajor) {
        mBioId = bioId;
        mName = name;
        mState = state;
        mParty = party;
        mDistrictRank = districtRank;
        mChamber = chamber;
        mIsMajor = isMajor;
    }

    public String getBioId() {
        return mBioId;
    }

    public String getName() {
        return mName;
    }

    /**
     * Append state with party in (D-NY) format
     * @return formatted (D-NY)
     */
    public String getPartyState() {
        return "("+mParty+"-"+mState+")";
    }

    public String getState() {
        return mState;
    }

    public String getChamber() {
        return mChamber;
    }

    public int getDistrictRank() {
        return mDistrictRank;
    }

    public boolean isMajor() {
        return mIsMajor;
    }
}
