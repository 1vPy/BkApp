
package com.roy.bkapp.model.music.search;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ArtistList {

    @SerializedName("album_num")
    private Long mAlbumNum;
    @SerializedName("artist_desc")
    private String mArtistDesc;
    @SerializedName("artist_id")
    private String mArtistId;
    @SerializedName("artist_source")
    private String mArtistSource;
    @SerializedName("author")
    private String mAuthor;
    @SerializedName("avatar_middle")
    private String mAvatarMiddle;
    @SerializedName("country")
    private String mCountry;
    @SerializedName("song_num")
    private Long mSongNum;
    @SerializedName("ting_uid")
    private String mTingUid;

    public Long getAlbumNum() {
        return mAlbumNum;
    }

    public void setAlbumNum(Long albumNum) {
        mAlbumNum = albumNum;
    }

    public String getArtistDesc() {
        return mArtistDesc;
    }

    public void setArtistDesc(String artistDesc) {
        mArtistDesc = artistDesc;
    }

    public String getArtistId() {
        return mArtistId;
    }

    public void setArtistId(String artistId) {
        mArtistId = artistId;
    }

    public String getArtistSource() {
        return mArtistSource;
    }

    public void setArtistSource(String artistSource) {
        mArtistSource = artistSource;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getAvatarMiddle() {
        return mAvatarMiddle;
    }

    public void setAvatarMiddle(String avatarMiddle) {
        mAvatarMiddle = avatarMiddle;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public Long getSongNum() {
        return mSongNum;
    }

    public void setSongNum(Long songNum) {
        mSongNum = songNum;
    }

    public String getTingUid() {
        return mTingUid;
    }

    public void setTingUid(String tingUid) {
        mTingUid = tingUid;
    }

}
