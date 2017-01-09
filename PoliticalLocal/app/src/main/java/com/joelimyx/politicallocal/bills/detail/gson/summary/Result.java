
package com.joelimyx.politicallocal.bills.detail.gson.summary;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("bill_id")
    @Expose
    private String billId;
    @SerializedName("keywords")
    @Expose
    private List<String> keywords = null;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("summary_short")
    @Expose
    private String summaryShort;

    public String getBillId() {
        return billId;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public String getSummary() {
        return summary;
    }

    public String getSummaryShort() {
        return summaryShort;
    }

}
