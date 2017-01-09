
package com.joelimyx.politicallocal.reps.gson.opensecret;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("contributors")
    @Expose
    private Contributors contributors;

    public Contributors getContributors() {
        return contributors;
    }

}
