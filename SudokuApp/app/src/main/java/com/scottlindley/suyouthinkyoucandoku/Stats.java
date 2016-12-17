package com.scottlindley.suyouthinkyoucandoku;

/**
 * Created by Scott Lindley on 12/12/2016.
 */

public class Stats {
    private int mHighscore, mRacesWon, mRacesLost, mBestTime;

    public Stats(int highscore, int racesWon, int racesLost, int bestTime) {
        mHighscore = highscore;
        mRacesWon = racesWon;
        mRacesLost = racesLost;
        mBestTime = bestTime;
    }

    public int getHighscore() {
        return mHighscore;
    }

    public int getRacesWon() {
        return mRacesWon;
    }

    public int getRacesLost() {
        return mRacesLost;
    }

    public int getBestTime() {
        return mBestTime;
    }

}
