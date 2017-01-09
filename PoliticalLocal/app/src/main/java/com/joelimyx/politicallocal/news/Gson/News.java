
package com.joelimyx.politicallocal.news.gson;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class News {

    @SerializedName("totalEstimatedMatches")
    @Expose
    private Integer totalEstimatedMatches;
    @SerializedName("value")
    @Expose
    private List<Value> value = null;

    /**
     * 
     * @return
     *     The totalEstimatedMatches
     */
    public Integer getTotalEstimatedMatches() {
        return totalEstimatedMatches;
    }

    /**
     * 
     * @return
     *     The value
     */
    public List<Value> getValue() {
        return value;
    }

}
