
package com.joelimyx.politicallocal.reps.gson.opensecret;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attributes_ {

    @SerializedName("org_name")
    @Expose
    private String orgName;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("pacs")
    @Expose
    private String pacs;
    @SerializedName("indivs")
    @Expose
    private String indivs;

    public String getOrgName() {
        return orgName;
    }


    public String getTotal() {
        return total;
    }

    public String getPacs() {
        return pacs;
    }

    public String getIndivs() {
        return indivs;
    }

}
