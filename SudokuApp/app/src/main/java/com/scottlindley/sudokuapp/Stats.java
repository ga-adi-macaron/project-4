package com.scottlindley.sudokuapp;

/**
 * Created by Scott Lindley on 12/12/2016.
 */

public class Stats {
    private int mHighscore, mRacesWon, mRacesLost;
    private String mBestTime;

    public Stats(int highscore, int racesWon, int racesLost, String bestTime) {
        mHighscore = highscore;
        mRacesWon = racesWon;
        mRacesLost = racesLost;
        mBestTime = bestTime;
    }

    public int getHighscore() {
        return mHighscore;
    }

    public void setHighscore(int highscore) {
        mHighscore = highscore;
    }

    public int getRacesWon() {
        return mRacesWon;
    }

    public void setRacesWon(int racesWon) {
        mRacesWon = racesWon;
    }

    public int getRacesLost() {
        return mRacesLost;
    }

    public void setRacesLost(int racesLost) {
        mRacesLost = racesLost;
    }

    public String getBestTime() {
        return mBestTime;
    }

    public void setBestTime(String bestTime) {
        mBestTime = bestTime;
    }
}
