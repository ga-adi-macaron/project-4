
package com.joelimyx.politicallocal.reps.gson.congress;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("bioguide_id")
    @Expose
    private String bioguideId;
    @SerializedName("chamber")
    @Expose
    private String chamber;
    @SerializedName("crp_id")
    @Expose
    private String crpId;
    @SerializedName("district")
    @Expose
    private Object district;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("in_office")
    @Expose
    private Boolean inOffice;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("middle_name")
    @Expose
    private String middleName;
    @SerializedName("oc_email")
    @Expose
    private String ocEmail;
    @SerializedName("party")
    @Expose
    private String party;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("state_name")
    @Expose
    private String stateName;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("twitter_id")
    @Expose
    private String twitterId;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("senate_class")
    @Expose
    private Integer senateClass;
    @SerializedName("term_end")
    @Expose
    private String termEnd;
    @SerializedName("term_start")
    @Expose
    private String termStart;

    public String getBioguideId() {
        return bioguideId;
    }

    public void setBioguideId(String bioguideId) {
        this.bioguideId = bioguideId;
    }

    public String getChamber() {
        return chamber;
    }

    public void setChamber(String chamber) {
        this.chamber = chamber;
    }

    public String getCrpId() {
        return crpId;
    }

    public void setCrpId(String crpId) {
        this.crpId = crpId;
    }

    public Object getDistrict() {
        return district;
    }

    public void setDistrict(Object district) {
        this.district = district;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Boolean getInOffice() {
        return inOffice;
    }

    public void setInOffice(Boolean inOffice) {
        this.inOffice = inOffice;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getOcEmail() {
        return ocEmail;
    }

    public void setOcEmail(String ocEmail) {
        this.ocEmail = ocEmail;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getSenateClass() {
        return senateClass;
    }

    public void setSenateClass(Integer senateClass) {
        this.senateClass = senateClass;
    }

    public String getTermEnd() {
        return termEnd;
    }

    public void setTermEnd(String termEnd) {
        this.termEnd = termEnd;
    }

    public String getTermStart() {
        return termStart;
    }

    public void setTermStart(String termStart) {
        this.termStart = termStart;
    }

}
