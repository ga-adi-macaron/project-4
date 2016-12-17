
package com.joelimyx.politicallocal.reps.detail.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attributes {

    @SerializedName("cid")
    @Expose
    private String cid;
    @SerializedName("firstlast")
    @Expose
    private String firstlast;
    @SerializedName("party")
    @Expose
    private String party;
    @SerializedName("office")
    @Expose
    private String office;
    @SerializedName("first_elected")
    @Expose
    private String firstElected;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("fax")
    @Expose
    private String fax;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("webform")
    @Expose
    private String webform;
    @SerializedName("bioguide_id")
    @Expose
    private String bioguideId;
    @SerializedName("twitter_id")
    @Expose
    private String twitterId;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getFirstlast() {
        return firstlast;
    }

    public void setFirstlast(String firstlast) {
        this.firstlast = firstlast;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getFirstElected() {
        return firstElected;
    }

    public void setFirstElected(String firstElected) {
        this.firstElected = firstElected;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWebform() {
        return webform;
    }

    public void setWebform(String webform) {
        this.webform = webform;
    }

    public String getBioguideId() {
        return bioguideId;
    }

    public void setBioguideId(String bioguideId) {
        this.bioguideId = bioguideId;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

}
