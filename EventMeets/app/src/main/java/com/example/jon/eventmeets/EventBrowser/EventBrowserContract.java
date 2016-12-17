package com.example.jon.eventmeets.EventBrowser;

import com.example.jon.eventmeets.Model.Category;

import java.util.List;

/**
 * Created by Jon on 12/16/2016.
 */

interface EventBrowserContract {
    interface View {
        void setBrowserAdapter(List<Category> list);
        void openCategory(Category category);
    }

    interface Presenter {
        void onActivityStarted();
    }
}
