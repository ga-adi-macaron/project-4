package com.joelimyx.politicallocal.bills;

/**
 * Created by Joe on 1/3/17.
 */

public class Voter {
    private String mBioID, mDecision;

    public Voter(String bioID, String decision) {
        mBioID = bioID;
        mDecision = decision;
    }

    public String getBioID() {
        return mBioID;
    }

    public String getDecision() {
        return mDecision;
    }
}
