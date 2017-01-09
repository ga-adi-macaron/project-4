
package com.joelimyx.politicallocal.bills.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bill {

    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("bill_uri")
    @Expose
    private String billUri;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("sponsor_uri")
    @Expose
    private String sponsorUri;
    @SerializedName("introduced_date")
    @Expose
    private String introducedDate;
    @SerializedName("cosponsors")
    @Expose
    private String cosponsors;
    @SerializedName("primary_subject")
    @Expose
    private String primarySubject;
    @SerializedName("latest_major_action_date")
    @Expose
    private String latestMajorActionDate;
    @SerializedName("latest_major_action")
    @Expose
    private String latestMajorAction;

    public String getNumber() {
        return number;
    }

    public String getBillUri() {
        return billUri;
    }

    public String getTitle() {
        return title;
    }

    public String getSponsorUri() {
        return sponsorUri;
    }

    public String getIntroducedDate() {
        return introducedDate;
    }

    public String getCosponsors() {
        return cosponsors;
    }

    public String getPrimarySubject() {
        return primarySubject;
    }

    public String getLatestMajorActionDate() {
        return latestMajorActionDate;
    }

    public String getLatestMajorAction() {
        return latestMajorAction;
    }

}
