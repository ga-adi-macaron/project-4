package com.example.jon.eventmeets.event_category;

import com.example.jon.eventmeets.model.EventParent;

/**
 * Created by Jon on 12/21/2016.
 */

public interface EventCategoriesContract {
    interface View {
        /**
         * If a category has subcategories, this will display them in the view above the
         * events under the all events header
         */
        void displayChildren();

        /**
         * Instead of opening a new activity, this will change the view by removing the subcategories
         * and displaying only the items matching the selected subcategory
         */
        void displaySubCategory(String subcategory);

        /**
         *
         * @param category - A string representing the type of {@link EventParent} for the adapter
         */
        void setRecyclerView(EventParent category);
    }

    interface Presenter {
        /**
         * Retrieves a list of all events from the remote data source and stores them locally in
         * before they are given to the view in a displayable format
         */
        void onAllEventsRequested(String category);

        /**
         * When a subcategory is selected in the view, this will filter out all events that do not
         * match the provided subcategory
         */
        void onSubCategoryEventsRequested(String subcategory);

        /**
         * Listens for the remote data source to return the first 25 events from the current cateogry
         * and passes the formatted list along to the view
         */
        void onAllEventsReceieved();

        /**
         * When the view is started, it will use this to pass the stringExtra from the intent in order
         * to determine whether or not the cateogry will need to display subcategories and events or
         * just subcategories
         */
        void onChildrenTypeSpecified(String type);
    }
}
