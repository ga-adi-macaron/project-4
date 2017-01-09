
package com.crocusgames.destinyinventorymanager.AccountInfoObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CharacterClass {

    @SerializedName("classHash")
    @Expose
    private Long classHash;
    @SerializedName("classType")
    @Expose
    private Long classType;
    @SerializedName("className")
    @Expose
    private String className;
    @SerializedName("classNameMale")
    @Expose
    private String classNameMale;
    @SerializedName("classNameFemale")
    @Expose
    private String classNameFemale;
    @SerializedName("classIdentifier")
    @Expose
    private String classIdentifier;
    @SerializedName("mentorVendorIdentifier")
    @Expose
    private String mentorVendorIdentifier;
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
     *     The classHash
     */
    public Long getClassHash() {
        return classHash;
    }

    /**
     * 
     * @param classHash
     *     The classHash
     */
    public void setClassHash(Long classHash) {
        this.classHash = classHash;
    }

    /**
     * 
     * @return
     *     The classType
     */
    public Long getClassType() {
        return classType;
    }

    /**
     * 
     * @param classType
     *     The classType
     */
    public void setClassType(Long classType) {
        this.classType = classType;
    }

    /**
     * 
     * @return
     *     The className
     */
    public String getClassName() {
        return className;
    }

    /**
     * 
     * @param className
     *     The className
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * 
     * @return
     *     The classNameMale
     */
    public String getClassNameMale() {
        return classNameMale;
    }

    /**
     * 
     * @param classNameMale
     *     The classNameMale
     */
    public void setClassNameMale(String classNameMale) {
        this.classNameMale = classNameMale;
    }

    /**
     * 
     * @return
     *     The classNameFemale
     */
    public String getClassNameFemale() {
        return classNameFemale;
    }

    /**
     * 
     * @param classNameFemale
     *     The classNameFemale
     */
    public void setClassNameFemale(String classNameFemale) {
        this.classNameFemale = classNameFemale;
    }

    /**
     * 
     * @return
     *     The classIdentifier
     */
    public String getClassIdentifier() {
        return classIdentifier;
    }

    /**
     * 
     * @param classIdentifier
     *     The classIdentifier
     */
    public void setClassIdentifier(String classIdentifier) {
        this.classIdentifier = classIdentifier;
    }

    /**
     * 
     * @return
     *     The mentorVendorIdentifier
     */
    public String getMentorVendorIdentifier() {
        return mentorVendorIdentifier;
    }

    /**
     * 
     * @param mentorVendorIdentifier
     *     The mentorVendorIdentifier
     */
    public void setMentorVendorIdentifier(String mentorVendorIdentifier) {
        this.mentorVendorIdentifier = mentorVendorIdentifier;
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
