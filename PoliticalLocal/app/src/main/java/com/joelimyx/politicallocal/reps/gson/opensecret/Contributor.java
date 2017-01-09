
package com.joelimyx.politicallocal.reps.gson.opensecret;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Contributor {

    @SerializedName("@attributes")
    @Expose
    private Attributes_ attributes;

    public Attributes_ getAttributes() {
        return attributes;
    }

}
