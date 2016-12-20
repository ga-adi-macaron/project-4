
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
    @SerializedName("committees")
    @Expose
    private String committees;
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

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBillUri() {
        return billUri;
    }

    public void setBillUri(String billUri) {
        this.billUri = billUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSponsorUri() {
        return sponsorUri;
    }

    public void setSponsorUri(String sponsorUri) {
        this.sponsorUri = sponsorUri;
    }

    public String getIntroducedDate() {
        return introducedDate;
    }

    public void setIntroducedDate(String introducedDate) {
        this.introducedDate = introducedDate;
    }

    public String getCosponsors() {
        return cosponsors;
    }

    public void setCosponsors(String cosponsors) {
        this.cosponsors = cosponsors;
    }

    public String getCommittees() {
        return committees;
    }

    public void setCommittees(String committees) {
        this.committees = committees;
    }

    public String getPrimarySubject() {
        return primarySubject;
    }

    public void setPrimarySubject(String primarySubject) {
        this.primarySubject = primarySubject;
    }

    public String getLatestMajorActionDate() {
        return latestMajorActionDate;
    }

    public void setLatestMajorActionDate(String latestMajorActionDate) {
        this.latestMajorActionDate = latestMajorActionDate;
    }

    public String getLatestMajorAction() {
        return latestMajorAction;
    }

    public void setLatestMajorAction(String latestMajorAction) {
        this.latestMajorAction = latestMajorAction;
    }

}
