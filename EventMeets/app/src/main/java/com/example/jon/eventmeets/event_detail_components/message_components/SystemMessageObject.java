package com.example.jon.eventmeets.event_detail_components.message_components;

/**
 * Created by Jon on 1/4/2017.
 */

public class SystemMessageObject extends SelfMessageObject {
    private String type;
    private String content;
    private String sender;

    public SystemMessageObject(String creator) {
        this.type = "start";
        this.content = creator+" started a new chat";
        this.sender = "system";
    }

    @Override
    public String getSender() {
        return sender;
    }
}
