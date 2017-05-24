
package com.roy.bkapp.model.music.search;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class SongInfo {

    @SerializedName("song_list")
    private List<SongList> mSongList;
    @SerializedName("total")
    private Long mTotal;

    public List<SongList> getSongList() {
        return mSongList;
    }

    public void setSongList(List<SongList> songList) {
        mSongList = songList;
    }

    public Long getTotal() {
        return mTotal;
    }

    public void setTotal(Long total) {
        mTotal = total;
    }

}
