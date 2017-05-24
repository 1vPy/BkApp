
package com.roy.bkapp.model.music.search;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Result {

    @SerializedName("album_info")
    private AlbumInfo mAlbumInfo;
    @SerializedName("artist_info")
    private ArtistInfo mArtistInfo;
    @SerializedName("playlist_info")
    private PlaylistInfo mPlaylistInfo;
    @SerializedName("query")
    private String mQuery;
    @SerializedName("rqt_type")
    private Long mRqtType;
    @SerializedName("song_info")
    private SongInfo mSongInfo;
    @SerializedName("syn_words")
    private String mSynWords;
    @SerializedName("tag_info")
    private TagInfo mTagInfo;
    @SerializedName("topic_info")
    private TopicInfo mTopicInfo;
    @SerializedName("user_info")
    private UserInfo mUserInfo;

    public AlbumInfo getAlbumInfo() {
        return mAlbumInfo;
    }

    public void setAlbumInfo(AlbumInfo albumInfo) {
        mAlbumInfo = albumInfo;
    }

    public ArtistInfo getArtistInfo() {
        return mArtistInfo;
    }

    public void setArtistInfo(ArtistInfo artistInfo) {
        mArtistInfo = artistInfo;
    }

    public PlaylistInfo getPlaylistInfo() {
        return mPlaylistInfo;
    }

    public void setPlaylistInfo(PlaylistInfo playlistInfo) {
        mPlaylistInfo = playlistInfo;
    }

    public String getQuery() {
        return mQuery;
    }

    public void setQuery(String query) {
        mQuery = query;
    }

    public Long getRqtType() {
        return mRqtType;
    }

    public void setRqtType(Long rqtType) {
        mRqtType = rqtType;
    }

    public SongInfo getSongInfo() {
        return mSongInfo;
    }

    public void setSongInfo(SongInfo songInfo) {
        mSongInfo = songInfo;
    }

    public String getSynWords() {
        return mSynWords;
    }

    public void setSynWords(String synWords) {
        mSynWords = synWords;
    }

    public TagInfo getTagInfo() {
        return mTagInfo;
    }

    public void setTagInfo(TagInfo tagInfo) {
        mTagInfo = tagInfo;
    }

    public TopicInfo getTopicInfo() {
        return mTopicInfo;
    }

    public void setTopicInfo(TopicInfo topicInfo) {
        mTopicInfo = topicInfo;
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
    }

}
