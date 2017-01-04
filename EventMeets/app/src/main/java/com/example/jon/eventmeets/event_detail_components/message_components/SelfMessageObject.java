package com.example.jon.eventmeets.event_detail_components.message_components;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Jon on 1/3/2017.
 */

public class SelfMessageObject {
    private String content;
    private String type;
    private String sender;

    public SelfMessageObject(String content) {
        this.content = content;
        sender = FirebaseAuth.getInstance().getCurrentUser().getUid();
        type = "message";
    }

    public SelfMessageObject() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }
}
