package com.lieblich.jon.playme.model;

import java.util.Map;

/**
 * Created by Jon on 1/6/2017.
 */

public class RegularPlayer {
    private String user;
    private String firstName;
    private Map<String, String> chats;

    public RegularPlayer(String user, String firstName) {
        this.user = user;
        this.firstName = firstName;
    }

    public RegularPlayer() {}

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Map<String, String> getChats() {
        return chats;
    }

    public void addChat(String id) {
        chats.put(id, "active");
    }

    public void addChats(String... ids) {
        for (String id : ids) {
            chats.put(id, "active");
        }
    }
}
