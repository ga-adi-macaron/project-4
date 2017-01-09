
package com.joelimyx.politicallocal.reps.gson.opensecret;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attributes {

    @SerializedName("source")
    @Expose
    private String source;

    public String getSource() {
        return source;
    }

}
