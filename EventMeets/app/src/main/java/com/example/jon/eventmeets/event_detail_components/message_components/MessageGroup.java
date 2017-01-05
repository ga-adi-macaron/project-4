package com.example.jon.eventmeets.event_detail_components.message_components;

import com.example.jon.eventmeets.model.AvailablePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MessageGroup {
    private Map<String, SelfMessageObject> messages;
    private List<AvailablePlayer> users;

    public MessageGroup() {
        messages = new HashMap<>();
        users = new ArrayList<>();
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

    /**
     * Called to add the 'created by' message when a new chat is started
     * @param creatorName the first name of the {@link AvailablePlayer} that started the room
     */
    public void addCreateMessage(String creatorName) {
        if(messages.size() == 0) {
            SystemMessageObject start = new SystemMessageObject(creatorName);
            messages.put("create", start);
        }
    }

    public void addMessage(String key, SelfMessageObject message) {
        messages.put(key, message);
    }
}
