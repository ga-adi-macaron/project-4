
package com.joelimyx.politicallocal.bills.detail.gson.propublica;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Action {

    @SerializedName("datetime")
    @Expose
    private String datetime;
    @SerializedName("description")
    @Expose
    private String description;

    public Action(String datetime, String description) {
        this.datetime = datetime;
        this.description = description;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
