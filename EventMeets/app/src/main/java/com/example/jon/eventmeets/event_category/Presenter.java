package com.example.jon.eventmeets.event_category;

import com.example.jon.eventmeets.model.EventParent;
import com.example.jon.eventmeets.model.conversation_models.ConversationEventParent;
import com.example.jon.eventmeets.model.drink_models.DrinkEventParent;
import com.example.jon.eventmeets.model.game_models.GamingEventParent;
import com.example.jon.eventmeets.model.nature_models.NatureEventParent;
import com.example.jon.eventmeets.model.taste_models.TasteEventParent;
import com.example.jon.eventmeets.model.theater_models.TheaterEventParent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Jon on 12/21/2016.
 */

class Presenter implements EventCategoriesContract.Presenter {
    private EventCategoriesContract.View mView;
    private FirebaseDatabase mDB;
    private DatabaseReference mRef;
    private List<EventParent> mEventList;
    private EventParent mForSubcategories;

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
        switch(type) {
            case "conversation":
                mForSubcategories = new ConversationEventParent();
                mView.setRecyclerView(mForSubcategories);
                break;
            case "drink":
                mForSubcategories = new DrinkEventParent();
                mView.setRecyclerView(mForSubcategories);
                break;
            case "game":
                mForSubcategories = new GamingEventParent();
                mView.setRecyclerView(mForSubcategories);
                break;
            case "nature":
                mForSubcategories = new NatureEventParent();
                mView.setRecyclerView(mForSubcategories);
                break;
            case "taste":
                mForSubcategories = new TasteEventParent();
                mView.setRecyclerView(mForSubcategories);
                break;
            case "theater":
                mForSubcategories = new TheaterEventParent();
                mView.setRecyclerView(mForSubcategories);
                break;
            default:
                mForSubcategories = null;
        }
    }
}
