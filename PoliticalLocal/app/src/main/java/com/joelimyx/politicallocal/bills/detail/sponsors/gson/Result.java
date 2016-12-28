
package com.joelimyx.politicallocal.bills.detail.sponsors.gson;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("sponsor")
    @Expose
    private String sponsor;
    @SerializedName("sponsor_id")
    @Expose
    private String sponsorId;
    @SerializedName("cosponsors")
    @Expose
    private List<Cosponsor> cosponsors = null;

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(String sponsorId) {
        this.sponsorId = sponsorId;
    }

    public List<Cosponsor> getCosponsors() {
        return cosponsors;
    }

    public void setCosponsors(List<Cosponsor> cosponsors) {
        this.cosponsors = cosponsors;
    }

}
