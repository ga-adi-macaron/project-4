package com.example.jon.eventmeets.event_detail_components.message_components;

/**
 * Created by Jon on 1/3/2017.
 */

public class SelfMessageObject {
    private String mContent;

    public SelfMessageObject(String content) {
        mContent = content;
    }

    public SelfMessageObject() {
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }
}
