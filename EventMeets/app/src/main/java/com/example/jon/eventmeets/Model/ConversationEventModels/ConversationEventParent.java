package com.example.jon.eventmeets.Model.ConversationEventModels;

import com.example.jon.eventmeets.Model.EventParent;

/**
 * Created by Jon on 12/16/2016.
 */

public class ConversationEventParent implements EventParent {

    @Override
    public String getParent() {
        return "Conversational";
    }
}
