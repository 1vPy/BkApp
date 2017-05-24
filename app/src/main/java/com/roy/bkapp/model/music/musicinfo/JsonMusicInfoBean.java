
package com.roy.bkapp.model.music.musicinfo;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class JsonMusicInfoBean {

    @SerializedName("error_code")
    private Long mErrorCode;
    @SerializedName("songinfo")
    private Songinfo mSonginfo;
    @SerializedName("songurl")
    private Songurl mSongurl;

    public Long getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(Long errorCode) {
        mErrorCode = errorCode;
    }

    public Songinfo getSonginfo() {
        return mSonginfo;
    }

    public void setSonginfo(Songinfo songinfo) {
        mSonginfo = songinfo;
    }

    public Songurl getSongurl() {
        return mSongurl;
    }

    public void setSongurl(Songurl songurl) {
        mSongurl = songurl;
    }

}
