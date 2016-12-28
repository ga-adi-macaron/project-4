
package com.joelimyx.politicallocal.bills.gson;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("congress")
    @Expose
    private String congress;
    @SerializedName("chamber")
    @Expose
    private String chamber;
    @SerializedName("bills")
    @Expose
    private List<Bill> bills = null;

    public String getCongress() {
        return congress;
    }

    public void setCongress(String congress) {
        this.congress = congress;
    }

    public String getChamber() {
        return chamber;
    }

    public void setChamber(String chamber) {
        this.chamber = chamber;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

}
