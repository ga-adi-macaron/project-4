
package com.joelimyx.politicallocal.news.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Provider {

    @SerializedName("_type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

}
