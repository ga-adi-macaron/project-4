
package com.crocusgames.destinyinventorymanager.AccountInfoObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LevelProgression {

    @SerializedName("dailyProgress")
    @Expose
    private Long dailyProgress;
    @SerializedName("weeklyProgress")
    @Expose
    private Long weeklyProgress;
    @SerializedName("currentProgress")
    @Expose
    private Long currentProgress;
    @SerializedName("level")
    @Expose
    private Long level;
    @SerializedName("step")
    @Expose
    private Long step;
    @SerializedName("progressToNextLevel")
    @Expose
    private Long progressToNextLevel;
    @SerializedName("nextLevelAt")
    @Expose
    private Long nextLevelAt;
    @SerializedName("progressionHash")
    @Expose
    private Long progressionHash;

    /**
     * 
     * @return
     *     The dailyProgress
     */
    public Long getDailyProgress() {
        return dailyProgress;
    }

    /**
     * 
     * @param dailyProgress
     *     The dailyProgress
     */
    public void setDailyProgress(Long dailyProgress) {
        this.dailyProgress = dailyProgress;
    }

    /**
     * 
     * @return
     *     The weeklyProgress
     */
    public Long getWeeklyProgress() {
        return weeklyProgress;
    }

    /**
     * 
     * @param weeklyProgress
     *     The weeklyProgress
     */
    public void setWeeklyProgress(Long weeklyProgress) {
        this.weeklyProgress = weeklyProgress;
    }

    /**
     * 
     * @return
     *     The currentProgress
     */
    public Long getCurrentProgress() {
        return currentProgress;
    }

    /**
     * 
     * @param currentProgress
     *     The currentProgress
     */
    public void setCurrentProgress(Long currentProgress) {
        this.currentProgress = currentProgress;
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
     *     The step
     */
    public Long getStep() {
        return step;
    }

    /**
     * 
     * @param step
     *     The step
     */
    public void setStep(Long step) {
        this.step = step;
    }

    /**
     * 
     * @return
     *     The progressToNextLevel
     */
    public Long getProgressToNextLevel() {
        return progressToNextLevel;
    }

    /**
     * 
     * @param progressToNextLevel
     *     The progressToNextLevel
     */
    public void setProgressToNextLevel(Long progressToNextLevel) {
        this.progressToNextLevel = progressToNextLevel;
    }

    /**
     * 
     * @return
     *     The nextLevelAt
     */
    public Long getNextLevelAt() {
        return nextLevelAt;
    }

    /**
     * 
     * @param nextLevelAt
     *     The nextLevelAt
     */
    public void setNextLevelAt(Long nextLevelAt) {
        this.nextLevelAt = nextLevelAt;
    }

    /**
     * 
     * @return
     *     The progressionHash
     */
    public Long getProgressionHash() {
        return progressionHash;
    }

    /**
     * 
     * @param progressionHash
     *     The progressionHash
     */
    public void setProgressionHash(Long progressionHash) {
        this.progressionHash = progressionHash;
    }

}
