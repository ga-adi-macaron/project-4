
package com.crocusgames.destinyinventorymanager.AccountInfoObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Gender {

    @SerializedName("genderHash")
    @Expose
    private Long genderHash;
    @SerializedName("genderType")
    @Expose
    private Long genderType;
    @SerializedName("genderName")
    @Expose
    private String genderName;
    @SerializedName("genderDescription")
    @Expose
    private String genderDescription;
    @SerializedName("hash")
    @Expose
    private Long hash;
    @SerializedName("index")
    @Expose
    private Long index;
    @SerializedName("redacted")
    @Expose
    private Boolean redacted;

    /**
     * 
     * @return
     *     The genderHash
     */
    public Long getGenderHash() {
        return genderHash;
    }

    /**
     * 
     * @param genderHash
     *     The genderHash
     */
    public void setGenderHash(Long genderHash) {
        this.genderHash = genderHash;
    }

    /**
     * 
     * @return
     *     The genderType
     */
    public Long getGenderType() {
        return genderType;
    }

    /**
     * 
     * @param genderType
     *     The genderType
     */
    public void setGenderType(Long genderType) {
        this.genderType = genderType;
    }

    /**
     * 
     * @return
     *     The genderName
     */
    public String getGenderName() {
        return genderName;
    }

    /**
     * 
     * @param genderName
     *     The genderName
     */
    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    /**
     * 
     * @return
     *     The genderDescription
     */
    public String getGenderDescription() {
        return genderDescription;
    }

    /**
     * 
     * @param genderDescription
     *     The genderDescription
     */
    public void setGenderDescription(String genderDescription) {
        this.genderDescription = genderDescription;
    }

    /**
     * 
     * @return
     *     The hash
     */
    public Long getHash() {
        return hash;
    }

    /**
     * 
     * @param hash
     *     The hash
     */
    public void setHash(Long hash) {
        this.hash = hash;
    }

    /**
     * 
     * @return
     *     The index
     */
    public Long getIndex() {
        return index;
    }

    /**
     * 
     * @param index
     *     The index
     */
    public void setIndex(Long index) {
        this.index = index;
    }

    /**
     * 
     * @return
     *     The redacted
     */
    public Boolean getRedacted() {
        return redacted;
    }

    /**
     * 
     * @param redacted
     *     The redacted
     */
    public void setRedacted(Boolean redacted) {
        this.redacted = redacted;
    }

}
