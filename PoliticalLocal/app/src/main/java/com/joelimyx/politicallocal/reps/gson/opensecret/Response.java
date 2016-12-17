
package com.joelimyx.politicallocal.reps.gson.opensecret;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("legislator")
    @Expose
    private List<Legislator> legislator = null;

    public List<Legislator> getLegislator() {
        return legislator;
    }

    public void setLegislator(List<Legislator> legislator) {
        this.legislator = legislator;
    }

}
