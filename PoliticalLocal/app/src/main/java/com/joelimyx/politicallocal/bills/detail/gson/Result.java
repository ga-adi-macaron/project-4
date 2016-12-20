
package com.joelimyx.politicallocal.bills.detail.gson;

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
    @SerializedName("versions")
    @Expose
    private List<Object> versions = null;
    @SerializedName("actions")
    @Expose
    private List<Action> actions = null;

    public String getCongress() {
        return congress;
    }

    public void setCongress(String congress) {
        this.congress = congress;
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getGpoPdfUri() {
        return gpoPdfUri;
    }

    public void setGpoPdfUri(String gpoPdfUri) {
        this.gpoPdfUri = gpoPdfUri;
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

    public String getPrimarySubject() {
        return primarySubject;
    }

    public void setPrimarySubject(String primarySubject) {
        this.primarySubject = primarySubject;
    }

    public Object getHousePassageVote() {
        return housePassageVote;
    }

    public void setHousePassageVote(Object housePassageVote) {
        this.housePassageVote = housePassageVote;
    }

    public Object getSenatePassageVote() {
        return senatePassageVote;
    }

    public void setSenatePassageVote(Object senatePassageVote) {
        this.senatePassageVote = senatePassageVote;
    }

    public List<Object> getVersions() {
        return versions;
    }

    public void setVersions(List<Object> versions) {
        this.versions = versions;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

}
