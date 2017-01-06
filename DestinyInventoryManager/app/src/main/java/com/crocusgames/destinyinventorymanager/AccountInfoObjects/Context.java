
package com.crocusgames.destinyinventorymanager.AccountInfoObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Context {

    @SerializedName("isFollowing")
    @Expose
    private Boolean isFollowing;
    @SerializedName("ignoreStatus")
    @Expose
    private IgnoreStatus ignoreStatus;

    /**
     * 
     * @return
     *     The isFollowing
     */
    public Boolean getIsFollowing() {
        return isFollowing;
    }

    /**
     * 
     * @param isFollowing
     *     The isFollowing
     */
    public void setIsFollowing(Boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

    /**
     * 
     * @return
     *     The ignoreStatus
     */
    public IgnoreStatus getIgnoreStatus() {
        return ignoreStatus;
    }

    /**
     * 
     * @param ignoreStatus
     *     The ignoreStatus
     */
    public void setIgnoreStatus(IgnoreStatus ignoreStatus) {
        this.ignoreStatus = ignoreStatus;
    }

}
