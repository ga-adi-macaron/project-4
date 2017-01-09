
package com.crocusgames.destinyinventorymanager.AccountInfoObjects;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DestinyAccount {

    @SerializedName("userInfo")
    @Expose
    private UserInfo userInfo;
    @SerializedName("grimoireScore")
    @Expose
    private Long grimoireScore;
    @SerializedName("characters")
    @Expose
    private List<Character> characters = null;
    @SerializedName("lastPlayed")
    @Expose
    private String lastPlayed;
    @SerializedName("versions")
    @Expose
    private Long versions;

    /**
     * 
     * @return
     *     The userInfo
     */
    public UserInfo getUserInfo() {
        return userInfo;
    }

    /**
     * 
     * @param userInfo
     *     The userInfo
     */
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * 
     * @return
     *     The grimoireScore
     */
    public Long getGrimoireScore() {
        return grimoireScore;
    }

    /**
     * 
     * @param grimoireScore
     *     The grimoireScore
     */
    public void setGrimoireScore(Long grimoireScore) {
        this.grimoireScore = grimoireScore;
    }

    /**
     * 
     * @return
     *     The characters
     */
    public List<Character> getCharacters() {
        return characters;
    }

    /**
     * 
     * @param characters
     *     The characters
     */
    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }

    /**
     * 
     * @return
     *     The lastPlayed
     */
    public String getLastPlayed() {
        return lastPlayed;
    }

    /**
     * 
     * @param lastPlayed
     *     The lastPlayed
     */
    public void setLastPlayed(String lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    /**
     * 
     * @return
     *     The versions
     */
    public Long getVersions() {
        return versions;
    }

    /**
     * 
     * @param versions
     *     The versions
     */
    public void setVersions(Long versions) {
        this.versions = versions;
    }

}
