
package com.joelimyx.politicallocal.reps.gson.opensecret;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Contributors {

    @SerializedName("@attributes")
    @Expose
    private Attributes attributes;
    @SerializedName("contributor")
    @Expose
    private List<Contributor> contributor = null;

    public Attributes getAttributes() {
        return attributes;
    }

    public List<Contributor> getContributor() {
        return contributor;
    }

}
