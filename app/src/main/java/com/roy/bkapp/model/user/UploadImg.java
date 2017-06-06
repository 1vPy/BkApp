
package com.roy.bkapp.model.user;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class UploadImg {

    @SerializedName("cdn")
    private String mCdn;
    @SerializedName("filename")
    private String mFilename;
    @SerializedName("url")
    private String mUrl;

    public String getCdn() {
        return mCdn;
    }

    public void setCdn(String cdn) {
        mCdn = cdn;
    }

    public String getFilename() {
        return mFilename;
    }

    public void setFilename(String filename) {
        mFilename = filename;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

}
