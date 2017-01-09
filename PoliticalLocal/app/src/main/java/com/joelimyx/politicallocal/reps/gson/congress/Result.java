
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

    public String getChamber() {
        return chamber;
    }

    public String getCrpId() {
        return crpId;
    }

    public Object getDistrict() {
        return district;
    }

    public String getFirstName() {
        return firstName;
    }

    public Boolean getInOffice() {
        return inOffice;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getOcEmail() {
        return ocEmail;
    }

    public String getParty() {
        return party;
    }

    public String getPhone() {
        return phone;
    }

    public String getState() {
        return state;
    }

    public String getStateName() {
        return stateName;
    }

    public String getTitle() {
        return title;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public String getWebsite() {
        return website;
    }

    public Integer getSenateClass() {
        return senateClass;
    }

    public String getTermEnd() {
        return termEnd;
    }

    public String getTermStart() {
        return termStart;
    }

}
