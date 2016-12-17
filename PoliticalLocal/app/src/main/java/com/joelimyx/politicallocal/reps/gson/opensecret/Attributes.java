
package com.joelimyx.politicallocal.reps.gson.opensecret;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attributes {

    @SerializedName("cid")
    @Expose
    private String cid;
    @SerializedName("firstlast")
    @Expose
    private String firstlast;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("party")
    @Expose
    private String party;
    @SerializedName("office")
    @Expose
    private String office;
    @SerializedName("first_elected")
    @Expose
    private String firstElected;
    @SerializedName("exit_code")
    @Expose
    private String exitCode;
    @SerializedName("phone")
    @Expose
    private String phone;
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public String getExitCode() {
        return exitCode;
    }

    public void setExitCode(String exitCode) {
        this.exitCode = exitCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
