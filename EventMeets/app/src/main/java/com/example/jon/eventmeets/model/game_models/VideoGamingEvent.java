package com.example.jon.eventmeets.model.game_models;

/**
 * Created by Jon on 12/16/2016.
 */

public class VideoGamingEvent {
    private int id;
    private String name;
    private String image;
    private String[] platforms;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String[] getPlatforms() {
        return platforms;
    }

    public void setPlatforms(String[] platforms) {
        this.platforms = platforms;
    }
}
