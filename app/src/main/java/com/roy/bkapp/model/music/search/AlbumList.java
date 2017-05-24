
package com.roy.bkapp.model.music.search;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class AlbumList {

    @SerializedName("album_desc")
    private String mAlbumDesc;
    @SerializedName("album_id")
    private String mAlbumId;
    @SerializedName("all_artist_id")
    private String mAllArtistId;
    @SerializedName("artist_id")
    private String mArtistId;
    @SerializedName("author")
    private String mAuthor;
    @SerializedName("company")
    private String mCompany;
    @SerializedName("hot")
    private Long mHot;
    @SerializedName("pic_small")
    private String mPicSmall;
    @SerializedName("publishtime")
    private String mPublishtime;
    @SerializedName("resource_type_ext")
    private String mResourceTypeExt;
    @SerializedName("title")
    private String mTitle;

    public String getAlbumDesc() {
        return mAlbumDesc;
    }

    public void setAlbumDesc(String albumDesc) {
        mAlbumDesc = albumDesc;
    }

    public String getAlbumId() {
        return mAlbumId;
    }

    public void setAlbumId(String albumId) {
        mAlbumId = albumId;
    }

    public String getAllArtistId() {
        return mAllArtistId;
    }

    public void setAllArtistId(String allArtistId) {
        mAllArtistId = allArtistId;
    }

    public String getArtistId() {
        return mArtistId;
    }

    public void setArtistId(String artistId) {
        mArtistId = artistId;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getCompany() {
        return mCompany;
    }

    public void setCompany(String company) {
        mCompany = company;
    }

    public Long getHot() {
        return mHot;
    }

    public void setHot(Long hot) {
        mHot = hot;
    }

    public String getPicSmall() {
        return mPicSmall;
    }

    public void setPicSmall(String picSmall) {
        mPicSmall = picSmall;
    }

    public String getPublishtime() {
        return mPublishtime;
    }

    public void setPublishtime(String publishtime) {
        mPublishtime = publishtime;
    }

    public String getResourceTypeExt() {
        return mResourceTypeExt;
    }

    public void setResourceTypeExt(String resourceTypeExt) {
        mResourceTypeExt = resourceTypeExt;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

}
