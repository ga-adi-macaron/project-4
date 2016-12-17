
package com.joelimyx.politicallocal.reps.detail.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("legislator")
    @Expose
    private Legislator legislator;

    public Legislator getLegislator() {
        return legislator;
    }

    public void setLegislator(Legislator legislator) {
        this.legislator = legislator;
    }

}
