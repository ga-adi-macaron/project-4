
package com.joelimyx.politicallocal.bills.detail.sponsors.detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("bioguide_id")
    @Expose
    private String bioguideId;
    @SerializedName("chamber")
    @Expose
    private String chamber;
    @SerializedName("district")
    @Expose
    private Integer district;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("middle_name")
    @Expose
    private Object middleName;
    @SerializedName("party")
    @Expose
    private String party;
    @SerializedName("senate_class")
    @Expose
    private Integer senateClass;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("title")
    @Expose
    private String title;

    public String getBioguideId() {
        return bioguideId;
    }

    public String getChamber() {
        return chamber;
    }

    public Integer getDistrict() {
        return district;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Object getMiddleName() {
        return middleName;
    }

    public String getParty() {
        return party;
    }

    public Integer getSenateClass() {
        return senateClass;
    }

    public String getState() {
        return state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
