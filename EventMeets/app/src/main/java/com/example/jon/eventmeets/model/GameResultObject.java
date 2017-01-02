package com.example.jon.eventmeets.model;

import java.util.List;

/**
 * Created by Jon on 1/1/2017.
 */

public class GameResultObject {
    private int id;
    private String summary;
    private String name;
    private List<ReleaseDateObject> release_dates;
    private List<ScreenshotObject> screenshots;
    private CoverObject cover;

    public GameResultObject() {}

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ReleaseDateObject> getRelease_dates() {
        return release_dates;
    }

    public void setRelease_dates(List<ReleaseDateObject> release_dates) {
        this.release_dates = release_dates;
    }

    public List<ScreenshotObject> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<ScreenshotObject> screenshots) {
        this.screenshots = screenshots;
    }

    public CoverObject getCover() {
        return cover;
    }

    public void setCover(CoverObject cover) {
        this.cover = cover;
    }
}
