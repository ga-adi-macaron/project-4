
package com.joelimyx.politicallocal.reps.detail.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Legislator {

    @SerializedName("@attributes")
    @Expose
    private Attributes attributes;

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

}
