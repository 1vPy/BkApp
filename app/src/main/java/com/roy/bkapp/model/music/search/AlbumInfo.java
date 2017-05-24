
package com.roy.bkapp.model.music.search;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class AlbumInfo {

    @SerializedName("album_list")
    private List<AlbumList> mAlbumList;
    @SerializedName("total")
    private Long mTotal;

    public List<AlbumList> getAlbumList() {
        return mAlbumList;
    }

    public void setAlbumList(List<AlbumList> albumList) {
        mAlbumList = albumList;
    }

    public Long getTotal() {
        return mTotal;
    }

    public void setTotal(Long total) {
        mTotal = total;
    }

}
