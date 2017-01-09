package com.lieblich.jon.playme.model;

/**
 * Created by Jon on 1/2/2017.
 */

public class DatabaseGameObject {
    private String id;
    private String title;
    private String screencap;
    private String cover;
    private String platform;

    public DatabaseGameObject() {}

    public DatabaseGameObject(String id, String title, String screencap, String cover, String platform) {
        this.id = id;
        this.title = title;
        this.screencap = screencap;
        this.cover = cover;
        this.platform = platform;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScreencap() {
        return screencap;
    }

    public void setScreencap(String screencap) {
        this.screencap = screencap;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
