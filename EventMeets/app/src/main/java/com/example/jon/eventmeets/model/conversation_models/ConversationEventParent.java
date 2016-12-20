package com.example.jon.eventmeets.model.conversation_models;

import com.example.jon.eventmeets.model.EventParent;

/**
 * Created by Jon on 12/16/2016.
 */

public class ConversationEventParent implements EventParent {

    @Override
    public String getParent() {
        return "Conversational";
    }
}
