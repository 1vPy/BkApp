package com.roy.bkapp.model.collection;

/**
 * Created by 1vPy(Roy) on 2017/3/28.
 */

public class MovieCollection {
    private Integer id;
    private String imageUrl;
    private String movieName;
    private String movieId;

    public MovieCollection() {

    }

    public MovieCollection(String imageUrl, String movieName, String movieId) {
        this.imageUrl = imageUrl;
        this.movieName = movieName;
        this.movieId = movieId;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }
}
