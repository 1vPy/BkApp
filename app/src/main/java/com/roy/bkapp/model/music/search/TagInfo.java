
package com.roy.bkapp.model.music.search;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class TagInfo {

    @SerializedName("total")
    private Long mTotal;

    public Long getTotal() {
        return mTotal;
    }

    public void setTotal(Long total) {
        mTotal = total;
    }

}
