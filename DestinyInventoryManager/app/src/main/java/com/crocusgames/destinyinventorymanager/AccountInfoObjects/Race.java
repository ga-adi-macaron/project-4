
package com.crocusgames.destinyinventorymanager.AccountInfoObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Race {

    @SerializedName("raceHash")
    @Expose
    private Long raceHash;
    @SerializedName("raceType")
    @Expose
    private Long raceType;
    @SerializedName("raceName")
    @Expose
    private String raceName;
    @SerializedName("raceNameMale")
    @Expose
    private String raceNameMale;
    @SerializedName("raceNameFemale")
    @Expose
    private String raceNameFemale;
    @SerializedName("raceDescription")
    @Expose
    private String raceDescription;
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
     *     The raceHash
     */
    public Long getRaceHash() {
        return raceHash;
    }

    /**
     * 
     * @param raceHash
     *     The raceHash
     */
    public void setRaceHash(Long raceHash) {
        this.raceHash = raceHash;
    }

    /**
     * 
     * @return
     *     The raceType
     */
    public Long getRaceType() {
        return raceType;
    }

    /**
     * 
     * @param raceType
     *     The raceType
     */
    public void setRaceType(Long raceType) {
        this.raceType = raceType;
    }

    /**
     * 
     * @return
     *     The raceName
     */
    public String getRaceName() {
        return raceName;
    }

    /**
     * 
     * @param raceName
     *     The raceName
     */
    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    /**
     * 
     * @return
     *     The raceNameMale
     */
    public String getRaceNameMale() {
        return raceNameMale;
    }

    /**
     * 
     * @param raceNameMale
     *     The raceNameMale
     */
    public void setRaceNameMale(String raceNameMale) {
        this.raceNameMale = raceNameMale;
    }

    /**
     * 
     * @return
     *     The raceNameFemale
     */
    public String getRaceNameFemale() {
        return raceNameFemale;
    }

    /**
     * 
     * @param raceNameFemale
     *     The raceNameFemale
     */
    public void setRaceNameFemale(String raceNameFemale) {
        this.raceNameFemale = raceNameFemale;
    }

    /**
     * 
     * @return
     *     The raceDescription
     */
    public String getRaceDescription() {
        return raceDescription;
    }

    /**
     * 
     * @param raceDescription
     *     The raceDescription
     */
    public void setRaceDescription(String raceDescription) {
        this.raceDescription = raceDescription;
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
