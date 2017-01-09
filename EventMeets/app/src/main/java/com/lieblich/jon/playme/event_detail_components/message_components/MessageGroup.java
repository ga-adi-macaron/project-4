package com.lieblich.jon.playme.event_detail_components.message_components;

import com.lieblich.jon.playme.model.AvailablePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MessageGroup {
    private String creator;
    private Map<String, SelfMessageObject> messages;
    private List<AvailablePlayer> users;

    public MessageGroup() {
        messages = new HashMap<>();
        users = new ArrayList<>();
    }

    public String getCreator() {
        return creator;
    }

    public Map<String,SelfMessageObject> getMessages() {
        return messages;
    }

    public List<AvailablePlayer> getUsers() {
        return users;
    }

    public void addUsers(AvailablePlayer... players) {
        for(int i = 0;i<players.length;i++) {
            users.add(players[i]);
        }
    }

    public void addCreateMessage(String creator) {
        this.creator = creator;
    }

    public void addMessage(String key, SelfMessageObject message) {
        messages.put(key, message);
    }
}
