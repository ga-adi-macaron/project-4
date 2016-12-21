package com.example.jon.eventmeets.event_category;

import com.example.jon.eventmeets.model.EventParent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Jon on 12/21/2016.
 */

public class Presenter implements EventCategoriesContract.Presenter {
    private EventCategoriesContract.View mView;
    private FirebaseDatabase mDB;
    private DatabaseReference mRef;
    private List<EventParent> mEventList;

    Presenter(EventCategoriesContract.View view) {
        mView = view;
        mDB = FirebaseDatabase.getInstance();
    }

    @Override
    public void onAllEventsRequested(String category) {
        mRef = mDB.getReference(category+"-event");
    }

    @Override
    public void onSubCategoryEventsRequested(String subcategory) {
        mRef.child(subcategory+"-event");
    }

    @Override
    public void onAllEventsReceieved() {

    }

    @Override
    public void onChildrenTypeSpecified(String type) {
        //These are the only categories with no subcategories
        if (type.equals("conversation")) {
            onAllEventsRequested(type);
        } else if(type.equals("drink")) {
            onAllEventsRequested(type);
        } else {
            onSubCategoryEventsRequested(type);
        }
    }
}
