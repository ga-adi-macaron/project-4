
package com.joelimyx.politicallocal.bills.detail.sponsors.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cosponsor {

    @SerializedName("cosponsor_id")
    @Expose
    private String cosponsorId;
    @SerializedName("name")
    @Expose
    private String name;

    public String getCosponsorId() {
        return cosponsorId;
    }

    public void setCosponsorId(String cosponsorId) {
        this.cosponsorId = cosponsorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
