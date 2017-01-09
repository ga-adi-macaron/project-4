package com.lieblich.jon.playme.event_detail_components.message_components;

/**
 * Created by Jon on 1/3/2017.
 */

public class MessageObject extends SelfMessageObject {
    private String content;
    private String sender;
    private String type;

    public MessageObject() {}

    public MessageObject(String content, String sender) {
        this.content = content;
        this.sender = sender;
        this.type = "message";
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public String getType() {
        return type;
    }
}
