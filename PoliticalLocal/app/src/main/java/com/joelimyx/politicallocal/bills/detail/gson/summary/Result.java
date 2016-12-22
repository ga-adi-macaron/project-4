
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

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummaryShort() {
        return summaryShort;
    }

    public void setSummaryShort(String summaryShort) {
        this.summaryShort = summaryShort;
    }

}
