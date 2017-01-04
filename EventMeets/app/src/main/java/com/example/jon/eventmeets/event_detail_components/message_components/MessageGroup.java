package com.example.jon.eventmeets.event_detail_components.message_components;

import com.example.jon.eventmeets.model.AvailablePlayer;

import java.util.ArrayList;
import java.util.List;


public class MessageGroup {
    private List<SelfMessageObject> messages;
    private List<AvailablePlayer> users;

    public MessageGroup() {
        messages = new ArrayList<>();
        users = new ArrayList<>();
    }

    public List<SelfMessageObject> getMessages() {
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
        MessageObject start = new MessageObject("Created by "+creatorName);
        start.setType("start");
        start.setSender(creatorName);
    }

    public void addMessage(SelfMessageObject message) {
        messages.add(message);
    }
}
