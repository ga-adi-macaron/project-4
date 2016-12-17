package com.example.jon.eventmeets.EventCategoryBrowser;

import com.example.jon.eventmeets.Model.Category;

import java.util.List;

interface EventBrowserContract {
    interface View {
        void setBrowserAdapter(List<Category> list);
        void openCategory(Category category);
    }

    interface Presenter {
        void onActivityStarted();
    }
}
