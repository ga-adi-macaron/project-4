
package com.crocusgames.destinyinventorymanager.AccountInfoObjects;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("destinyAccounts")
    @Expose
    private List<DestinyAccount> destinyAccounts = null;
    @SerializedName("bungieNetUser")
    @Expose
    private BungieNetUser bungieNetUser;
    @SerializedName("clans")
    @Expose
    private List<Object> clans = null;
    @SerializedName("relatedGroups")
    @Expose
    private RelatedGroups relatedGroups;
    @SerializedName("destinyAccountErrors")
    @Expose
    private List<Object> destinyAccountErrors = null;

    /**
     * 
     * @return
     *     The destinyAccounts
     */
    public List<DestinyAccount> getDestinyAccounts() {
        return destinyAccounts;
    }

    /**
     * 
     * @param destinyAccounts
     *     The destinyAccounts
     */
    public void setDestinyAccounts(List<DestinyAccount> destinyAccounts) {
        this.destinyAccounts = destinyAccounts;
    }

    /**
     * 
     * @return
     *     The bungieNetUser
     */
    public BungieNetUser getBungieNetUser() {
        return bungieNetUser;
    }

    /**
     * 
     * @param bungieNetUser
     *     The bungieNetUser
     */
    public void setBungieNetUser(BungieNetUser bungieNetUser) {
        this.bungieNetUser = bungieNetUser;
    }

    /**
     * 
     * @return
     *     The clans
     */
    public List<Object> getClans() {
        return clans;
    }

    /**
     * 
     * @param clans
     *     The clans
     */
    public void setClans(List<Object> clans) {
        this.clans = clans;
    }

    /**
     * 
     * @return
     *     The relatedGroups
     */
    public RelatedGroups getRelatedGroups() {
        return relatedGroups;
    }

    /**
     * 
     * @param relatedGroups
     *     The relatedGroups
     */
    public void setRelatedGroups(RelatedGroups relatedGroups) {
        this.relatedGroups = relatedGroups;
    }

    /**
     * 
     * @return
     *     The destinyAccountErrors
     */
    public List<Object> getDestinyAccountErrors() {
        return destinyAccountErrors;
    }

    /**
     * 
     * @param destinyAccountErrors
     *     The destinyAccountErrors
     */
    public void setDestinyAccountErrors(List<Object> destinyAccountErrors) {
        this.destinyAccountErrors = destinyAccountErrors;
    }

}
