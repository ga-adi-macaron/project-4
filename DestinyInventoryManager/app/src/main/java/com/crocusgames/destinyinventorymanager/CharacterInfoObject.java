package com.crocusgames.destinyinventorymanager;

/**
 * Created by Serkan on 13/12/16.
 */

public class CharacterInfoObject {
    private String genderName, raceName, className, emblemUrl, backgroundUrl, normalLevel,
            lightLevel, characterId;

    public CharacterInfoObject(String backgroundUrl, String characterId, String className,
                               String emblemUrl, String genderName, String lightLevel,
                               String normalLevel, String raceName) {
        this.backgroundUrl = backgroundUrl;
        this.characterId = characterId;
        this.className = className;
        this.emblemUrl = emblemUrl;
        this.genderName = genderName;
        this.lightLevel = lightLevel;
        this.normalLevel = normalLevel;
        this.raceName = raceName;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getCharacterId() {
        return characterId;
    }

    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getEmblemUrl() {
        return emblemUrl;
    }

    public void setEmblemUrl(String emblemUrl) {
        this.emblemUrl = emblemUrl;
    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    public String getLightLevel() {
        return lightLevel;
    }

    public void setLightLevel(String lightLevel) {
        this.lightLevel = lightLevel;
    }

    public String getNormalLevel() {
        return normalLevel;
    }

    public void setNormalLevel(String normalLevel) {
        this.normalLevel = normalLevel;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }
}
