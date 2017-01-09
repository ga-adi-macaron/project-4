
package com.joelimyx.politicallocal.bills.detail.gson.propublica;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("congress")
    @Expose
    private String congress;
    @SerializedName("bill")
    @Expose
    private String bill;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("sponsor")
    @Expose
    private String sponsor;
    @SerializedName("gpo_pdf_uri")
    @Expose
    private String gpoPdfUri;
    @SerializedName("introduced_date")
    @Expose
    private String introducedDate;
    @SerializedName("cosponsors")
    @Expose
    private String cosponsors;
    @SerializedName("primary_subject")
    @Expose
    private String primarySubject;
    @SerializedName("house_passage_vote")
    @Expose
    private Object housePassageVote;
    @SerializedName("senate_passage_vote")
    @Expose
    private Object senatePassageVote;
    @SerializedName("latest_major_action_date")
    @Expose
    private String latest_major_action_date;
    @SerializedName("latest_major_action")
    @Expose
    private String latest_major_action;
    @SerializedName("actions")
    @Expose
    private List<Action> actions = null;

    public String getCongress() {
        return congress;
    }

    public String getBill() {
        return bill;
    }

    public String getTitle() {
        return title;
    }

    public String getSponsor() {
        return sponsor;
    }

    public String getGpoPdfUri() {
        return gpoPdfUri;
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

    public Object getHousePassageVote() {
        return housePassageVote;
    }

    public Object getSenatePassageVote() {
        return senatePassageVote;
    }

    public String  getLatest_major_action_date() {
        return latest_major_action_date;
    }

    public String getLatest_major_action() {
        return latest_major_action;
    }

    public List<Action> getActions() {
        return actions;
    }

}
