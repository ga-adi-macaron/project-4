
package com.joelimyx.politicallocal.search.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class History {

    @SerializedName("enacted")
    @Expose
    private boolean enacted;

    public boolean isEnacted() {
        return enacted;
    }

    public void setEnacted(boolean enacted) {
        this.enacted = enacted;
    }

}
