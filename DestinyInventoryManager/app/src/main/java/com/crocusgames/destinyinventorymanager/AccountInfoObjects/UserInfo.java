
package com.crocusgames.destinyinventorymanager.AccountInfoObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo {

    @SerializedName("iconPath")
    @Expose
    private String iconPath;
    @SerializedName("membershipType")
    @Expose
    private Long membershipType;
    @SerializedName("membershipId")
    @Expose
    private String membershipId;
    @SerializedName("displayName")
    @Expose
    private String displayName;

    /**
     * 
     * @return
     *     The iconPath
     */
    public String getIconPath() {
        return iconPath;
    }

    /**
     * 
     * @param iconPath
     *     The iconPath
     */
    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    /**
     * 
     * @return
     *     The membershipType
     */
    public Long getMembershipType() {
        return membershipType;
    }

    /**
     * 
     * @param membershipType
     *     The membershipType
     */
    public void setMembershipType(Long membershipType) {
        this.membershipType = membershipType;
    }

    /**
     * 
     * @return
     *     The membershipId
     */
    public String getMembershipId() {
        return membershipId;
    }

    /**
     * 
     * @param membershipId
     *     The membershipId
     */
    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    /**
     * 
     * @return
     *     The displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 
     * @param displayName
     *     The displayName
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
