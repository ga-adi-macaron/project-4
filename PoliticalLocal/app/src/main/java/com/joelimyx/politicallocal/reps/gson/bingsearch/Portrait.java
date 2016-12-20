
package com.joelimyx.politicallocal.reps.gson.bingsearch;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Portrait {

    @SerializedName("value")
    @Expose
    private List<Value> value = null;

    public List<Value> getValue() {
        return value;
    }

    public void setValue(List<Value> value) {
        this.value = value;
    }

}
