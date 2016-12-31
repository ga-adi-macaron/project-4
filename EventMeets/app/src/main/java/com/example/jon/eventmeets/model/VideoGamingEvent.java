package com.example.jon.eventmeets.model;

import com.example.jon.eventmeets.event_detail_components.VideoGameImages;
import com.example.jon.eventmeets.event_detail_components.VideoGamePlatforms;

import java.util.ArrayList;

/**
 * Created by Jon on 12/16/2016.
 */

public class VideoGamingEvent {
    private int id;
    private VideoGameImages image;
    private String name;
    private ArrayList<VideoGamePlatforms> platforms;
    private String resource_type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VideoGameImages getImage() {
        return image;
    }

    public void setImage(VideoGameImages image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<VideoGamePlatforms> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(ArrayList<VideoGamePlatforms> platforms) {
        this.platforms = platforms;
    }

    public String getResource_type() {
        return resource_type;
    }

    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }
}
