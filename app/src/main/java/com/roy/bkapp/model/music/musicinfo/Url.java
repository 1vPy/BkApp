
package com.roy.bkapp.model.music.musicinfo;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Url {

    @SerializedName("can_load")
    private Boolean mCanLoad;
    @SerializedName("can_see")
    private Long mCanSee;
    @SerializedName("down_type")
    private Long mDownType;
    @SerializedName("file_bitrate")
    private Long mFileBitrate;
    @SerializedName("file_duration")
    private Long mFileDuration;
    @SerializedName("file_extension")
    private String mFileExtension;
    @SerializedName("file_link")
    private String mFileLink;
    @SerializedName("file_size")
    private Long mFileSize;
    @SerializedName("free")
    private Long mFree;
    @SerializedName("hash")
    private String mHash;
    @SerializedName("is_udition_url")
    private Double mIsUditionUrl;
    @SerializedName("original")
    private Double mOriginal;
    @SerializedName("preload")
    private Double mPreload;
    @SerializedName("replay_gain")
    private String mReplayGain;
    @SerializedName("show_link")
    private String mShowLink;
    @SerializedName("song_file_id")
    private Double mSongFileId;

    public Boolean getCanLoad() {
        return mCanLoad;
    }

    public void setCanLoad(Boolean mCanLoad) {
        this.mCanLoad = mCanLoad;
    }

    public Long getCanSee() {
        return mCanSee;
    }

    public void setCanSee(Long mCanSee) {
        this.mCanSee = mCanSee;
    }

    public Long getDownType() {
        return mDownType;
    }

    public void setDownType(Long mDownType) {
        this.mDownType = mDownType;
    }

    public Long getFileBitrate() {
        return mFileBitrate;
    }

    public void setFileBitrate(Long mFileBitrate) {
        this.mFileBitrate = mFileBitrate;
    }

    public Long getFileDuration() {
        return mFileDuration;
    }

    public void setFileDuration(Long mFileDuration) {
        this.mFileDuration = mFileDuration;
    }

    public String getFileExtension() {
        return mFileExtension;
    }

    public void setFileExtension(String mFileExtension) {
        this.mFileExtension = mFileExtension;
    }

    public String getFileLink() {
        return mFileLink;
    }

    public void setFileLink(String mFileLink) {
        this.mFileLink = mFileLink;
    }

    public Long getFileSize() {
        return mFileSize;
    }

    public void setFileSize(Long mFileSize) {
        this.mFileSize = mFileSize;
    }

    public Long getFree() {
        return mFree;
    }

    public void setFree(Long mFree) {
        this.mFree = mFree;
    }

    public String getHash() {
        return mHash;
    }

    public void setHash(String mHash) {
        this.mHash = mHash;
    }

    public Double getIsUditionUrl() {
        return mIsUditionUrl;
    }

    public void setIsUditionUrl(Double mIsUditionUrl) {
        this.mIsUditionUrl = mIsUditionUrl;
    }

    public Double getOriginal() {
        return mOriginal;
    }

    public void setOriginal(Double mOriginal) {
        this.mOriginal = mOriginal;
    }

    public Double getPreload() {
        return mPreload;
    }

    public void setPreload(Double mPreload) {
        this.mPreload = mPreload;
    }

    public String getReplayGain() {
        return mReplayGain;
    }

    public void setReplayGain(String mReplayGain) {
        this.mReplayGain = mReplayGain;
    }

    public String getShowLink() {
        return mShowLink;
    }

    public void setShowLink(String mShowLink) {
        this.mShowLink = mShowLink;
    }

    public Double getSongFileId() {
        return mSongFileId;
    }

    public void setSongFileId(Double mSongFileId) {
        this.mSongFileId = mSongFileId;
    }
}
