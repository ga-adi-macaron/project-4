
package com.crocusgames.destinyinventorymanager.AccountInfoObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IgnoreStatus {

    @SerializedName("isIgnored")
    @Expose
    private Boolean isIgnored;
    @SerializedName("ignoreFlags")
    @Expose
    private Long ignoreFlags;

    /**
     * 
     * @return
     *     The isIgnored
     */
    public Boolean getIsIgnored() {
        return isIgnored;
    }

    /**
     * 
     * @param isIgnored
     *     The isIgnored
     */
    public void setIsIgnored(Boolean isIgnored) {
        this.isIgnored = isIgnored;
    }

    /**
     * 
     * @return
     *     The ignoreFlags
     */
    public Long getIgnoreFlags() {
        return ignoreFlags;
    }

    /**
     * 
     * @param ignoreFlags
     *     The ignoreFlags
     */
    public void setIgnoreFlags(Long ignoreFlags) {
        this.ignoreFlags = ignoreFlags;
    }

}
