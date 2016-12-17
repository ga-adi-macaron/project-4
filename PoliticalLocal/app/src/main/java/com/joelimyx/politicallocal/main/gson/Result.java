
package com.joelimyx.politicallocal.main.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("district")
    @Expose
    private Integer district;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getDistrict() {
        return district;
    }

    public void setDistrict(Integer district) {
        this.district = district;
    }

}
