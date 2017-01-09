
package com.joelimyx.politicallocal.search.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("bill_type")
    @Expose
    private String billType;
    @SerializedName("number")
    @Expose
    private long number;
    @SerializedName("congress")
    @Expose
    private long congress;
    @SerializedName("last_action_at")
    @Expose
    private String lastActionAt;
    @SerializedName("official_title")
    @Expose
    private String officialTitle;
    @SerializedName("history")
    @Expose
    private History history;

    public String getBillType() {
        return billType;
    }

    public long getNumber() {
        return number;
    }

    public long getCongress() {
        return congress;
    }

    public String getLastActionAt() {
        return lastActionAt;
    }

    public String getOfficialTitle() {
        return officialTitle;
    }

    public History getHistory() {
        return history;
    }

}
