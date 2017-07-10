
package com.roy.bkapp.model.collection;

import com.google.gson.annotations.SerializedName;

public class MovieCloudResult {

    @SerializedName("createdAt")
    private String mCreatedAt;
    @SerializedName("imageUrl")
    private String mImageUrl;
    @SerializedName("movieId")
    private String mMovieId;
    @SerializedName("movieName")
    private String mMovieName;
    @SerializedName("objectId")
    private String mObjectId;
    @SerializedName("updatedAt")
    private String mUpdatedAt;
    @SerializedName("username")
    private String mUsername;

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getMovieId() {
        return mMovieId;
    }

    public void setMovieId(String movieId) {
        mMovieId = movieId;
    }

    public String getMovieName() {
        return mMovieName;
    }

    public void setMovieName(String movieName) {
        mMovieName = movieName;
    }

    public String getObjectId() {
        return mObjectId;
    }

    public void setObjectId(String objectId) {
        mObjectId = objectId;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

}
