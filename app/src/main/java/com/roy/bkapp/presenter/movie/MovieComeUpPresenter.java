package com.roy.bkapp.presenter.movie;

import com.roy.bkapp.http.RequestCallback;
import com.roy.bkapp.http.api.douban.DoubanApiService;
import com.roy.bkapp.model.movie.JsonMovieBean;
import com.roy.bkapp.presenter.BasePresenter;
import com.roy.bkapp.ui.view.movie.MovieComeUpView;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/5/11.
 */

public class MovieComeUpPresenter extends BasePresenter<MovieComeUpView> {

    private DoubanApiService mDoubanApiService;

    @Inject
    public MovieComeUpPresenter(DoubanApiService doubanApiService){
        mDoubanApiService = doubanApiService;
    }

    public void getMovieComeUp(int start,int count){
        mDoubanApiService.getComingMovie(start, count, new RequestCallback<JsonMovieBean>() {
            @Override
            public void onSuccess(JsonMovieBean jsonMovieBean) {
                if(isAttached()){
                    getView().movieComeUp(jsonMovieBean);
                }
            }

            @Override
            public void onFailure(String message) {
                if(isAttached()){
                    getView().showError(message);
                }

            }
        });
    }

}
