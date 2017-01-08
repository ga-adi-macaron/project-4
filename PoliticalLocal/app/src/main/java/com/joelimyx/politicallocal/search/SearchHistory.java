package com.joelimyx.politicallocal.search;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by Joe on 1/6/17.
 */

//// TODO: 1/7/17 DELETE??
public class SearchHistory implements SearchSuggestion {
    private String mLastInput;
    private boolean mIsHistory;

    public SearchHistory(String lastInput) {
        mLastInput = lastInput;
        mIsHistory = false;
    }

    public SearchHistory(Parcel parcel) {
        mLastInput = parcel.readString();
        mIsHistory = parcel.readInt() != 0;
    }

    @Override
    public String getBody() {
        return mLastInput;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mLastInput);
        dest.writeInt(mIsHistory ? 1:0);
    }
    public static final Creator<SearchHistory> CREATOR = new Creator<SearchHistory>() {
        @Override
        public SearchHistory createFromParcel(Parcel in) {
            return new SearchHistory(in);
        }

        @Override
        public SearchHistory[] newArray(int size) {
            return new SearchHistory[size];
        }
    };
}
