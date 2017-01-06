
package com.crocusgames.destinyinventorymanager.AccountInfoObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Character {

    @SerializedName("characterId")
    @Expose
    private String characterId;
    @SerializedName("raceHash")
    @Expose
    private Long raceHash;
    @SerializedName("genderHash")
    @Expose
    private Long genderHash;
    @SerializedName("classHash")
    @Expose
    private Long classHash;
    @SerializedName("emblemHash")
    @Expose
    private Long emblemHash;
    @SerializedName("race")
    @Expose
    private Race race;
    @SerializedName("gender")
    @Expose
    private Gender gender;
    @SerializedName("characterClass")
    @Expose
    private CharacterClass characterClass;
    @SerializedName("emblemPath")
    @Expose
    private String emblemPath;
    @SerializedName("backgroundPath")
    @Expose
    private String backgroundPath;
    @SerializedName("level")
    @Expose
    private Long level;
    @SerializedName("powerLevel")
    @Expose
    private Long powerLevel;
    @SerializedName("dateLastPlayed")
    @Expose
    private String dateLastPlayed;
    @SerializedName("membershipId")
    @Expose
    private String membershipId;
    @SerializedName("membershipType")
    @Expose
    private Long membershipType;
    @SerializedName("levelProgression")
    @Expose
    private LevelProgression levelProgression;
    @SerializedName("isPrestigeLevel")
    @Expose
    private Boolean isPrestigeLevel;
    @SerializedName("genderType")
    @Expose
    private Long genderType;
    @SerializedName("classType")
    @Expose
    private Long classType;

    /**
     * 
     * @return
     *     The characterId
     */
    public String getCharacterId() {
        return characterId;
    }

    /**
     * 
     * @param characterId
     *     The characterId
     */
    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }

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
     *     The emblemHash
     */
    public Long getEmblemHash() {
        return emblemHash;
    }

    /**
     * 
     * @param emblemHash
     *     The emblemHash
     */
    public void setEmblemHash(Long emblemHash) {
        this.emblemHash = emblemHash;
    }

    /**
     * 
     * @return
     *     The race
     */
    public Race getRace() {
        return race;
    }

    /**
     * 
     * @param race
     *     The race
     */
    public void setRace(Race race) {
        this.race = race;
    }

    /**
     * 
     * @return
     *     The gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * 
     * @param gender
     *     The gender
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * 
     * @return
     *     The characterClass
     */
    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    /**
     * 
     * @param characterClass
     *     The characterClass
     */
    public void setCharacterClass(CharacterClass characterClass) {
        this.characterClass = characterClass;
    }

    /**
     * 
     * @return
     *     The emblemPath
     */
    public String getEmblemPath() {
        return emblemPath;
    }

    /**
     * 
     * @param emblemPath
     *     The emblemPath
     */
    public void setEmblemPath(String emblemPath) {
        this.emblemPath = emblemPath;
    }

    /**
     * 
     * @return
     *     The backgroundPath
     */
    public String getBackgroundPath() {
        return backgroundPath;
    }

    /**
     * 
     * @param backgroundPath
     *     The backgroundPath
     */
    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }

    /**
     * 
     * @return
     *     The level
     */
    public Long getLevel() {
        return level;
    }

    /**
     * 
     * @param level
     *     The level
     */
    public void setLevel(Long level) {
        this.level = level;
    }

    /**
     * 
     * @return
     *     The powerLevel
     */
    public Long getPowerLevel() {
        return powerLevel;
    }

    /**
     * 
     * @param powerLevel
     *     The powerLevel
     */
    public void setPowerLevel(Long powerLevel) {
        this.powerLevel = powerLevel;
    }

    /**
     * 
     * @return
     *     The dateLastPlayed
     */
    public String getDateLastPlayed() {
        return dateLastPlayed;
    }

    /**
     * 
     * @param dateLastPlayed
     *     The dateLastPlayed
     */
    public void setDateLastPlayed(String dateLastPlayed) {
        this.dateLastPlayed = dateLastPlayed;
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
     *     The levelProgression
     */
    public LevelProgression getLevelProgression() {
        return levelProgression;
    }

    /**
     * 
     * @param levelProgression
     *     The levelProgression
     */
    public void setLevelProgression(LevelProgression levelProgression) {
        this.levelProgression = levelProgression;
    }

    /**
     * 
     * @return
     *     The isPrestigeLevel
     */
    public Boolean getIsPrestigeLevel() {
        return isPrestigeLevel;
    }

    /**
     * 
     * @param isPrestigeLevel
     *     The isPrestigeLevel
     */
    public void setIsPrestigeLevel(Boolean isPrestigeLevel) {
        this.isPrestigeLevel = isPrestigeLevel;
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
}
