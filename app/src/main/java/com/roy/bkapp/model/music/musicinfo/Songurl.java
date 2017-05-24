
package com.roy.bkapp.model.music.musicinfo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class Songurl {

    @SerializedName("url")
    private List<Url> mUrl;

    public List<Url> getUrl() {
        return mUrl;
    }

    public void setUrl(List<Url> url) {
        mUrl = url;
    }

}
