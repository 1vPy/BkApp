package com.roy.bkapp.model.collection;

/**
 * Created by 1vPy(Roy) on 2017/3/28.
 */

public class MovieCollection {
    private Integer id;
    private String movieName;
    private String movieType;
    private String movieStar;
    private String movieId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieType() {
        return movieType;
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
    }

    public String getMovieStar() {
        return movieStar;
    }

    public void setMovieStar(String movieStar) {
        this.movieStar = movieStar;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }
}
