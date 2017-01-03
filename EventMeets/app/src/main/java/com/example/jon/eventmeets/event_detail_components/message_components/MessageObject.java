package com.example.jon.eventmeets.event_detail_components.message_components;

/**
 * Created by Jon on 1/3/2017.
 */

public class MessageObject extends SelfMessageObject {
    private String mContent;
    private String mSenderName;
    private String mPhotoURL;

    public MessageObject(String content){
        super(content);
    }

    public MessageObject(String content, String senderName, String photoURL) {
        mContent = content;
        mSenderName = senderName;
        mPhotoURL = photoURL;
    }

    @Override
    public String getContent() {
        return mContent;
    }

    @Override
    public void setContent(String content) {
        mContent = content;
    }

    public String getSenderName() {
        return mSenderName;
    }

    public void setSenderName(String senderName) {
        mSenderName = senderName;
    }

    public String getPhotoURL() {
        return mPhotoURL;
    }

    public void setPhotoURL(String photoURL) {
        mPhotoURL = photoURL;
    }
}
