
package com.joelimyx.politicallocal.news.Gson;

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
     * @param totalEstimatedMatches
     *     The totalEstimatedMatches
     */
    public void setTotalEstimatedMatches(Integer totalEstimatedMatches) {
        this.totalEstimatedMatches = totalEstimatedMatches;
    }

    /**
     * 
     * @return
     *     The value
     */
    public List<Value> getValue() {
        return value;
    }

    /**
     * 
     * @param value
     *     The value
     */
    public void setValue(List<Value> value) {
        this.value = value;
    }

}
